package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.SummaryTodayV;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.repository.SummaryTodayVRepository;
import com.dbiz.app.tenantservice.service.DashBoardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.tenantDto.SummaryTodayVDto;
import org.common.dbiz.dto.tenantDto.TempSalesRevenueDto;
import org.common.dbiz.dto.tenantDto.TempSalesSummaryDto;
import org.common.dbiz.dto.tenantDto.dashboard.request.TotalRevenueReqDto;
import org.common.dbiz.dto.tenantDto.dashboard.response.*;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.tenantRequest.DashBoardCredential;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashBoardServiceImpl implements DashBoardService {

    private final SummaryTodayVRepository summaryTodayVRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final EntityManager entityManager;

    private StringBuilder sql ;

    private final DataSource dataSource;
    /**
     * @param tenantId
     * @param orgId
     * @return
     */
    @Override
    public GlobalReponse getSummaryToday(Integer tenantId, Integer orgId) {
        Specification<SummaryTodayV> spec = (root, query, cb) -> {
            return cb.and(
                    cb.equal(root.get("tenantId"),tenantId),
                    cb.equal(root.get("orgId"), orgId)
            );
        };
        List<SummaryTodayV> result = summaryTodayVRepository.findAll(spec);
//        List<SummaryTodayVDto> resultDto = result.stream().map(e -> modelMapper.map(e, SummaryTodayVDto.class)).collect(Collectors.toList());
        String sql = "select count_order_processing, amount_order_processing " +
                " from pos.d_summary_today_v where d_tenant_id =:tenantId and d_org_id =:orgId";
        List<SummaryTodayVDto> resultDto = result.stream().map(e -> {

            List<Map<String, Object>> resultList = entityManager.createNativeQuery(sql.toString())
                    .setParameter("tenantId", tenantId)
                    .setParameter("orgId", orgId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();

            SummaryTodayVDto dto = modelMapper.map(e, SummaryTodayVDto.class);
            log.info("Vao day");
            for (Map<String, Object> row : resultList) {
                log.info("Vao day 1");
                dto.setCountOrderProcessing(ParseHelper.INT.parse(row.get("count_order_processing")));
                dto.setAmountOrderProcessing(ParseHelper.BIGDECIMAL.parse(row.get("amount_order_processing")));
            }
            log.info("Vao day 2");
            return dto;
        }).collect(Collectors.toList());


        return GlobalReponse.builder()
                .data(resultDto)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * @param rq
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse salesRevenue(DashBoardCredential rq) {

        if (rq.getOrgId() == null) {
            rq.setOrgId(0);
        }

//        StoredProcedureQuery storedProcedure = entityManager
//                .createStoredProcedureQuery("pos.d_revenue_procedure");
//
//        // Đăng ký tham số IN
//        storedProcedure.registerStoredProcedureParameter("i_org_or_all", String.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("i_org_id", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("i_tenant_id", Integer.class, ParameterMode.IN);
//        storedProcedure.registerStoredProcedureParameter("i_query_type", String.class, ParameterMode.IN);
//
//        // Thiết lập giá trị cho các tham số
//        storedProcedure.setParameter("i_org_or_all", rq.getOrg());
//        storedProcedure.setParameter("i_org_id", rq.getOrgId());
//        storedProcedure.setParameter("i_tenant_id", AuditContext.getAuditInfo().getTenantId());
//        storedProcedure.setParameter("i_query_type", rq.getQueryType());
//
//        storedProcedure.execute();

        Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_revenue_procedure(:orgOrAll, :orgId, :tenantId, :queryType)");
        queryProcedure.setParameter("orgOrAll", rq.getOrg());
        queryProcedure.setParameter("orgId", rq.getOrgId());
        queryProcedure.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        queryProcedure.setParameter("queryType", rq.getQueryType());

        queryProcedure.executeUpdate();




        sql = new StringBuilder("select d_org_id, max_amount, revenue_object, amount_incre_object from d_temp_sales_revenue where d_tenant_id =:tenantId and d_org_id =:orgId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        query.setParameter("orgId",rq.getOrgId());
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("d_org_id", StandardBasicTypes.INTEGER)
                .addScalar("max_amount", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("revenue_object", StandardBasicTypes.STRING) // Ánh xạ kiểu jsonb thành String
                .addScalar("amount_incre_object", StandardBasicTypes.STRING)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//                ;

        List<Map<String, Object>> results =query.getResultList();

        List<TempSalesRevenueDto>listResults = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map<String, Object> row : results) {
            String revenueObjectJson = ParseHelper.STRING.parse(row.get("revenue_object"));
            String amountIncreObjectJson = ParseHelper.STRING.parse(row.get("amount_incre_object"));
            JsonNode revenueObjectNode  = null;
            JsonNode amountIncreObjectNode = null;
            try {
                revenueObjectNode = objectMapper.readTree(revenueObjectJson);
                amountIncreObjectNode = objectMapper.readTree(amountIncreObjectJson);
            }catch (Exception e) {
                log.error("Error parse json: {}", e.getMessage());
            }

            TempSalesRevenueDto item = TempSalesRevenueDto.builder()
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .maxAmount(ParseHelper.BIGDECIMAL.parse(row.get("max_amount")))
                    .revenueObject(revenueObjectNode)
                    .amountIncreObject(amountIncreObjectNode)

                    .build();
            listResults.add(item);
        }

        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }


    @Override
    public GlobalReponse getTotalRevenue(TotalRevenueReqDto rq) {


        List<TotalRevenueRespDto> listResults = new ArrayList<>();
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_dashboard_revenue(?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getDashBoardType());
            callableStatement.setString(3, rq.getDisplayType());
            callableStatement.setString(4, rq.getOrgOrAll());
            callableStatement.setInt(5, rq.getOrgId());
            callableStatement.setInt(6, AuditContext.getAuditInfo().getTenantId());
            callableStatement.setString(7, rq.getQueryType());
            callableStatement.setString(8, rq.getStartDate());
            callableStatement.setString(9, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                TotalRevenueRespDto item = null;
                if(rq.getDashBoardType().equals("CYCLE")){
                    item = TotalRevenueRespDto.builder()
                            .orgName(rs.getString("org_name"))
                            .orgTotalRevenue(rs.getBigDecimal("org_total_revenue"))
                            .revenuePercentage(rs.getBigDecimal("revenue_percentage"))
                            .revenueByPercentage(rs.getBigDecimal("revenue_by_percentage"))
                            .build();
                }else if (rq.getDashBoardType().equals("LINE")){
                    item = TotalRevenueRespDto.builder()
                            .tenantId(rs.getInt("d_tenant_id"))
                            .orgId(rs.getInt("org_id"))
                            .orgName(rs.getString("org_name"))
                            .revenueObject(rs.getString("revenue_object"))
                            .build();
                }

                listResults.add(item);
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getRevenueByEmp(TotalRevenueReqDto rq) {

        List<RevenueByEmpDto> listResults = new ArrayList<>();
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_dashboard_rev_emp(?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getDashBoardType());
            callableStatement.setString(3, rq.getOrgOrAll());
            callableStatement.setInt(4, rq.getOrgId());
            callableStatement.setInt(5, AuditContext.getAuditInfo().getTenantId());
            callableStatement.setString(6, rq.getQueryType());
            callableStatement.setString(7, rq.getStartDate());
            callableStatement.setString(8, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                RevenueByEmpDto item = null;
                if(rq.getDashBoardType().equals("COLUMN")){
                    item = RevenueByEmpDto.builder()
                            .orgId(rs.getInt("org_id"))
                            .orgName(rs.getString("org_name"))
                            .empSaleName(rs.getString("emp_sale_name"))
                            .empSaleId(rs.getString("emp_sale_id"))
                            .totalAmount(rs.getBigDecimal("total_amount"))
                            .build();
                }else if (rq.getDashBoardType().equals("CYCLE")){
                    item = RevenueByEmpDto.builder()
                            .empSaleName(rs.getString("emp_sale_name"))
                            .empSaleId(rs.getString("emp_sale_id"))
                            .totalAmount(rs.getBigDecimal("total_amount"))
                            .amountPercent(rs.getBigDecimal("amount_percent"))
                            .build();
                }

                listResults.add(item);
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getRevenueByServiceType(TotalRevenueReqDto rq) {
        List<RevenueByServiceTypeDto> listResults = new ArrayList<>();
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_dashboard_service_type( ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setInt(4, AuditContext.getAuditInfo().getTenantId());
            callableStatement.setString(5, rq.getQueryType());
            callableStatement.setString(6, rq.getStartDate());
            callableStatement.setString(7, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                RevenueByServiceTypeDto item = RevenueByServiceTypeDto.builder()
                        .serviceType(rs.getString("service_type"))
                        .amount(rs.getBigDecimal("amount"))
                        .orderNo(rs.getInt("order_no"))
                        .totalAmount(rs.getBigDecimal("total_amount"))
                        .totalOrderNo(rs.getInt("total_order_no"))
                        .amountPercent(rs.getBigDecimal("amount_percent"))
                        .build();


                listResults.add(item);
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getCancelPrSummary(TotalRevenueReqDto rq) {

        List<CancelPrSummaryDto> listResults = new ArrayList<>();
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_dashboard_cancel( ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getOrgOrAll());
            callableStatement.setInt(3, rq.getOrgId());
            callableStatement.setInt(4, AuditContext.getAuditInfo().getTenantId());
            callableStatement.setString(5, rq.getQueryType());
            callableStatement.setString(6, rq.getStartDate());
            callableStatement.setString(7, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                CancelPrSummaryDto item = CancelPrSummaryDto.builder()
                        .cancelReasonId(rs.getInt("cancel_reason_id"))
                        .reasonName(rs.getString("reason_name"))
                        .qty(rs.getBigDecimal("qty"))
                        .totalQty(rs.getBigDecimal("total_qty"))
                        .qtyPercent(rs.getBigDecimal("qty_percent"))
                        .build();
                listResults.add(item);
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    @Override
    public GlobalReponse getTop10Product(TotalRevenueReqDto rq) {

        List<Top10ProductDto> listResults = new ArrayList<>();
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }
        try(Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            CallableStatement callableStatement = connection.prepareCall("{? = call get_dashboard_product(?, ?, ?, ?, ?, ?, ?, ?)}");
            callableStatement.registerOutParameter(1, Types.REF_CURSOR); // PostgreSQL specific
            callableStatement.setString(2, rq.getDashBoardType());
            callableStatement.setString(3, rq.getDisplayType());
            callableStatement.setString(4, rq.getOrgOrAll());
            callableStatement.setInt(5, rq.getOrgId());
            callableStatement.setInt(6, AuditContext.getAuditInfo().getTenantId());
            callableStatement.setString(7, rq.getQueryType());
            callableStatement.setString(8, rq.getStartDate());
            callableStatement.setString(9, rq.getEndDate());
            callableStatement.execute();
            // Execute the query and retrieve the refcursor
            ResultSet rs = (ResultSet) callableStatement.getObject(1);

            // Process the result set
            while (rs.next()) {
                Top10ProductDto item = Top10ProductDto.builder()
                        .productId(rs.getInt("d_product_id"))
                        .productName(rs.getString("product_name"))
                        .productCode(rs.getString("product_code"))
                        .imageUrl(rs.getString("image_url"))
                        .build();
                if(rq.getDashBoardType().equals("CYCLE")){
                    if(rq.getDisplayType().equals("REVENUE")){
                        item.setAmountPercent(rs.getBigDecimal("amount_percent"));
                    }else if (rq.getDisplayType().equals("QUANTITY")){
                        item.setQtyPercent(rs.getBigDecimal("qty_percent"));
                    }
                }

                if(rq.getDisplayType().equals("REVENUE")){
                    item.setTotalAmount(rs.getBigDecimal("total_amount"));
                }else if (rq.getDisplayType().equals("QUANTITY")){
                    item.setTotalQty(rs.getBigDecimal("total_qty"));
                }

                listResults.add(item);
            }
        }catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }


        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    /**
     * @param rq
     * @return
     */
    @Override
    @Transactional
    public GlobalReponse salesSummary(DashBoardCredential rq) {
        if(rq.getOrgId() == null){
            rq.setOrgId(0);
        }

        Query queryProcedure = entityManager.createNativeQuery("CALL pos.d_top_product_procedure(:orgOrAll, :orgId, :tenantId, :queryType)");
        queryProcedure.setParameter("orgOrAll", rq.getOrg());
        queryProcedure.setParameter("orgId", rq.getOrgId());
        queryProcedure.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        queryProcedure.setParameter("queryType", rq.getQueryType());

        queryProcedure.executeUpdate();



        sql = new StringBuilder("select d_org_id, max_qty, product_object,sequence_number_object from d_temp_sales_summary where d_tenant_id =:tenantId and d_org_id =:orgId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        query.setParameter("orgId",rq.getOrgId());
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("product_object", StandardBasicTypes.STRING) // Ánh xạ kiểu jsonb thành String
                .addScalar("sequence_number_object", StandardBasicTypes.STRING)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//                ;

        List<Map<String, Object>> results =query.getResultList();

        List<TempSalesSummaryDto>listResults = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (Map<String, Object> row : results) {
            String productObjectJson = ParseHelper.STRING.parse(row.get("product_object"));
            String sequenceNumberObject = ParseHelper.STRING.parse(row.get("sequence_number_object"));
            JsonNode productObjectNode  = null;
            JsonNode sequenceNumberNode = null;
            try {
                productObjectNode = objectMapper.readTree(productObjectJson);
                sequenceNumberNode = objectMapper.readTree(sequenceNumberObject);
            }catch (Exception e) {
                log.error("Error parse json: {}", e.getMessage());
            }

            TempSalesSummaryDto item = TempSalesSummaryDto.builder()
                    .orgId(ParseHelper.INT.parse(row.get("d_org_id")))
                    .maxQty(ParseHelper.INT.parse(row.get("max_qty")))
                    .productObject(productObjectNode)
                    .sequenceNumberObject(sequenceNumberNode)
                    .build();
            listResults.add(item);
        }

        return GlobalReponse.builder()
                .data(listResults)
                .message(messageSource.getMessage("successfully",null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .errors("")
                .build();
    }

    private List<Map<String, Object>> excuteQuery(String sql, Map<String, Object> parameters) {
        Query query = entityManager.createNativeQuery(sql);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> results = query.getResultList();
        return results;
    }
}
