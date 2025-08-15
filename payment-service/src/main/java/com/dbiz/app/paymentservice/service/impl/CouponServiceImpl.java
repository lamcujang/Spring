package com.dbiz.app.paymentservice.service.impl;


import com.dbiz.app.paymentservice.domain.Coupon;
import com.dbiz.app.paymentservice.repository.CouponRepository;
import com.dbiz.app.paymentservice.service.CouponService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponRespDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Override
    public GlobalReponsePagination findAll(Object queryRequest) {

        log.info("*** Coupon, service; fetch all Coupon ***");

        CouponParamDto dto = (CouponParamDto) queryRequest;

        // Calculate the offset for pagination
        int offset = (dto.getPage() - 1) * dto.getPageSize();

        StringBuilder sql = new StringBuilder(
                "SELECT d_coupon_id, " +
                        "d_tenant_id, " +
                        "d_org_id," +
                        "balance_amount," +
                        "code, " +
                        "description," +
                        "is_available," +
                        "d_pos_terminal_id," +
                        "is_active," +
                        "terminal_name," +
                        "d_vendor_id," +
                        "vendor_name," +
                        "d_customer_id," +
                        "customer_name,org_name " +
                        " FROM pos.d_coupon_v  WHERE d_tenant_id = :tenantId "


        );
//        String sqlCheckPos = "select d_pos_terminal_id from pos.d_pos_terminal where d_pos_terminal_id =:posTerminalId and is_default = 'Y' and d_tenant_id = :tenantId";
//        List<BigDecimal> resultList = entityManager.createNativeQuery(sqlCheckPos)
//                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
//                .setParameter("posTerminalId", dto.getPosTerminalId())
//                .getResultList();
//        BigDecimal posterminalCheck = resultList.isEmpty() ? BigDecimal.ZERO : resultList.get(0);

//        if (posterminalCheck.equals(BigDecimal.ZERO)) {
//            if(dto.getPosTerminalId() != null){
//                sql.append(" and d_pos_terminal_id = :posTerminalId ");
//            }
//        }

        if(dto.getOrgId() != null){
            sql.append(" and d_org_id = :orgId ");
        }
        if(dto.getCode() != null){
            sql.append(" and lower(code) like lower( '%' || :couponCode  || '%' ) ");
        }

        if (dto.getCustomerId() != null) {
            sql.append(" and (d_customer_id = :customerId or d_customer_id is null) ");
        }

        if (dto.getVendorId() != null) {
            sql.append(" and d_vendor_id = :vendorId ");
        }

        if (dto.getIsActive() != null) {
            sql.append(" and is_active = :isActive ");
        }

        if (dto.getIsAvailable() != null) {
            sql.append(" and is_available = :isAvailable ");
        }
        sql.append(" ORDER BY " + dto.getSortBy() + " " + dto.getOrder());
        sql.append(" LIMIT :limit OFFSET :offset ");

        Query couponQuery = entityManager.createNativeQuery(sql.toString());
        couponQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if(dto.getOrgId() != null){
            couponQuery.setParameter("orgId", dto.getOrgId());
        }

//        if (posterminalCheck.equals(BigDecimal.ZERO)) {
//            if(dto.getPosTerminalId() != null){
//                couponQuery.setParameter("posTerminalId", dto.getPosTerminalId());
//            }
//        }


        if (dto.getCode() != null) {
            couponQuery.setParameter("couponCode", dto.getCode());
        }

        if (dto.getCustomerId() != null) {
            couponQuery.setParameter("customerId", dto.getCustomerId());
        }
        if (dto.getVendorId() != null) {
            couponQuery.setParameter("vendorId", dto.getVendorId());
        }
//
        if (dto.getIsActive() != null) {
            couponQuery.setParameter("isActive", dto.getIsActive());
        }

        if (dto.getIsAvailable() != null) {
            couponQuery.setParameter("isAvailable", dto.getIsAvailable());
        }


        couponQuery.setParameter("limit", dto.getPageSize());
        couponQuery.setParameter("offset", dto.getPage());


        // Fetch the paginated results
        List<Map<String, Object>> results = couponQuery.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CouponRespDto> listCouponRespDto = new ArrayList<>();

        for (Map<String, Object> result : results) {
            CouponRespDto couponRespDto = CouponRespDto.builder()
                    .id(ParseHelper.INT.parse(result.get("d_coupon_id")))
                    .tenantId(ParseHelper.INT.parse(result.get("d_tenant_id")))
                    .orgId(ParseHelper.INT.parse(result.get("d_org_id")))
                    .orgName(ParseHelper.STRING.parse(result.get("org_name")))
                    .code(ParseHelper.STRING.parse(result.get("code")))
                    .balanceAmount(ParseHelper.BIGDECIMAL.parse(result.get("balance_amount")))
                    .description(ParseHelper.STRING.parse(result.get("description")))
                    .isAvailable(ParseHelper.STRING.parse(result.get("is_available")))
                    .isActive(ParseHelper.STRING.parse(result.get("is_active")))
                    .posTerminal(PosTerminalDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_pos_terminal_id")))
                            .name(ParseHelper.STRING.parse(result.get("terminal_name")))
                            .build())
                    .vendor(VendorDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_vendor_id")))
                            .name(ParseHelper.STRING.parse(result.get("vendor_name")))
                            .build())
                    .customer(CustomerDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_customer_id")))
                            .name(ParseHelper.STRING.parse(result.get("customer_name")))
                            .build())
                    .build();
            listCouponRespDto.add(couponRespDto);
        }


        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM pos.d_coupon_v  WHERE d_tenant_id = :tenantId "
                );

//        if (posterminalCheck.equals(BigDecimal.ZERO)) {
//            if (dto.getPosTerminalId() != null){
//                countSql.append(" and d_pos_terminal_id = :posTerminalId ");
//            }
//
//        }

        if(dto.getOrgId()!= null)
        {
            countSql.append(" and d_org_id = :orgId ");
        }
        if(dto.getCode() != null){
            countSql.append(" and lower(code) like lower( '%' || :couponCode  || '%' ) ");
        }

        if (dto.getCustomerId() != null) {
            countSql.append(" and d_customer_id = :customerId ");
        }
        if (dto.getVendorId() != null) {
            countSql.append(" and d_vendor_id = :vendorId ");
        }

        if (dto.getIsActive() != null) {
            countSql.append(" and is_active = :isActive ");
        }

        if (dto.getIsAvailable() != null) {
            countSql.append(" and is_available = :isAvailable ");
        }

        // Get the total number of records
        Query totalRecordsQuery = entityManager.createNativeQuery(countSql.toString());
        totalRecordsQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());

        if(dto.getOrgId()!= null)
        {
            totalRecordsQuery.setParameter("orgId", dto.getOrgId());
        }
//        if (posterminalCheck.equals(BigDecimal.ZERO)) {
//            if (dto.getPosTerminalId() != null){
//                totalRecordsQuery.setParameter("posTerminalId", dto.getPosTerminalId());
//            }
//        }
        if (dto.getCode() != null) {
            totalRecordsQuery.setParameter("couponCode", dto.getCode());
        }

        if (dto.getCustomerId() != null) {
            totalRecordsQuery.setParameter("customerId", dto.getCustomerId());
        }
        if (dto.getVendorId() != null) {
            totalRecordsQuery.setParameter("vendorId", dto.getVendorId());
        }
        if (dto.getIsActive() != null) {
            totalRecordsQuery.setParameter("isActive", dto.getIsActive());
        }

        if (dto.getIsAvailable() != null) {
            totalRecordsQuery.setParameter("isAvailable", dto.getIsAvailable());
        }


        Long totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();


        return GlobalReponsePagination.builder()
                .data(listCouponRespDto)
                .pageSize(dto.getPageSize())
                .currentPage(dto.getPage())
                .totalItems(totalRecords)
                .totalPages((int) Math.ceil((double)totalRecords / dto.getPageSize()))
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
    }

    @Override
    public GlobalReponse save(Object Dto) {

        log.info("*** Coupon, service; save Coupon ***");
        CouponDto dto = (CouponDto) Dto;
        GlobalReponse response = new GlobalReponse();
        Coupon coupon = null;
        try {
            if (dto.getId() != null) {
                coupon = couponRepository.findById(dto.getId()).orElse(null);
                if (coupon != null) {
                    modelMapper.map(dto, coupon);
                    coupon = couponRepository.save(coupon);
                    response.setStatus(HttpStatus.OK.value());
                }
            } else {

                Optional<Coupon> couponOptional = couponRepository.findByCodeAndTenantId(dto.getCode(), AuditContext.getAuditInfo().getTenantId());
                if (couponOptional.isPresent()) {
                    throw new PosException("Coupon with code already exists");
                }
                coupon = modelMapper.map(dto, Coupon.class);
                coupon.setTenantId(AuditContext.getAuditInfo().getTenantId());

                if(dto.getOrgId() == null){

                    coupon.setOrgId(0);
                }

                coupon = couponRepository.save(coupon);
                response.setStatus(HttpStatus.CREATED.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        response.setData(coupon != null ? modelMapper.map(coupon, CouponDto.class) : null);
        response.setMessage("Coupon saved successfully");
        return response;
    }

    @Override
    public GlobalReponse deleteById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponse findById(Integer id) {
        return null;
    }

    @Override
    public GlobalReponsePagination findAllForPayment(CouponParamDto queryRequest) {

        log.info("*** Coupon, service; fetch all Coupon for payment ***");

        CouponParamDto dto = (CouponParamDto) queryRequest;

        // Calculate the offset for pagination
        int offset = (dto.getPage() - 1) * dto.getPageSize();

        StringBuilder sql = new StringBuilder(
                "SELECT d_coupon_id, " +
                        "d_tenant_id, " +
                        "d_org_id, " +
                        "balance_amount, " +
                        "code, " +
                        "description, " +
                        "is_available, " +
                        "d_pos_terminal_id, " +
                        "is_active, " +
                        "terminal_name, " +
                        "d_vendor_id, " +
                        "vendor_name, " +
                        "d_customer_id, " +
                        "customer_name, " +
                        "org_name " +
                        "FROM pos.d_coupon_v " +
                        "WHERE d_tenant_id = :tenantId " +
                        "AND (d_org_id = :orgId OR d_org_id = 0) " +
                        "AND (d_pos_terminal_id = :posTerminalId OR d_pos_terminal_id is null) " +
                        "AND is_active = 'Y' " +
                        "AND is_available = 'Y' " +
                        "AND (d_customer_id = :customerId OR d_customer_id IS NULL)"
        );

        if (dto.getCode() != null) {
            sql.append(" and code like :couponCode ");
        }

        sql.append(" ORDER BY created DESC ");
        sql.append(" LIMIT :limit OFFSET :offset ");

        Query couponQuery = entityManager.createNativeQuery(sql.toString());
        couponQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        couponQuery.setParameter("orgId", dto.getOrgId());
        couponQuery.setParameter("posTerminalId", dto.getPosTerminalId());
        couponQuery.setParameter("customerId", dto.getCustomerId()== null ? 0 : dto.getCustomerId());


        if (dto.getCode() != null) {
            couponQuery.setParameter("couponCode", "%" + dto.getCode() + "%");
        }
//        dto.setPageSize(100);
        couponQuery.setParameter("limit", dto.getPageSize());
        couponQuery.setParameter("offset", dto.getPage() * dto.getPageSize());


        // Fetch the paginated results
        List<Map<String, Object>> results = couponQuery.unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CouponRespDto> listCouponRespDto = new ArrayList<>();

        for (Map<String, Object> result : results) {
            CouponRespDto couponRespDto = CouponRespDto.builder()
                    .id(ParseHelper.INT.parse(result.get("d_coupon_id")))
                    .tenantId(ParseHelper.INT.parse(result.get("d_tenant_id")))
                    .orgId(ParseHelper.INT.parse(result.get("d_org_id")))
                    .orgName(ParseHelper.STRING.parse(result.get("org_name")))
                    .code(ParseHelper.STRING.parse(result.get("code")))
                    .balanceAmount(ParseHelper.BIGDECIMAL.parse(result.get("balance_amount")))
                    .description(ParseHelper.STRING.parse(result.get("description")))
                    .isAvailable(ParseHelper.STRING.parse(result.get("is_available")))
                    .isActive(ParseHelper.STRING.parse(result.get("is_active")))
                    .posTerminal(PosTerminalDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_pos_terminal_id")))
                            .name(ParseHelper.STRING.parse(result.get("terminal_name")))
                            .build())
                    .vendor(VendorDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_vendor_id")))
                            .name(ParseHelper.STRING.parse(result.get("vendor_name")))
                            .build())
                    .customer(CustomerDto.builder()
                            .id(ParseHelper.INT.parse(result.get("d_customer_id")))
                            .name(ParseHelper.STRING.parse(result.get("customer_name")))
                            .build())
                    .build();
            listCouponRespDto.add(couponRespDto);
        }


        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM pos.d_coupon_v  WHERE d_tenant_id = :tenantId " +
                " AND (d_org_id = :orgId OR d_org_id = 0) " +
                " AND (d_pos_terminal_id = :posTerminalId OR d_pos_terminal_id is null) " +
                " AND is_active = 'Y' AND is_available = 'Y' " +
                " AND (d_customer_id = :customerId OR d_customer_id IS NULL)");

        if (dto.getCode() != null) {
            countSql.append(" and code like :couponCode ");
        }

        // Get the total number of records
        Query totalRecordsQuery = entityManager.createNativeQuery(countSql.toString());
        totalRecordsQuery.setParameter("tenantId", AuditContext.getAuditInfo().getTenantId());
        totalRecordsQuery.setParameter("orgId", dto.getOrgId());
        totalRecordsQuery.setParameter("posTerminalId", dto.getPosTerminalId());
        totalRecordsQuery.setParameter("customerId", dto.getCustomerId());
        if (dto.getCode() != null) {
            totalRecordsQuery.setParameter("couponCode", "%" + dto.getCode() + "%");
        }

        Long totalRecords = ((Number) totalRecordsQuery.getSingleResult()).longValue();

        return GlobalReponsePagination.builder()
                .data(listCouponRespDto)
                .pageSize(dto.getPageSize())
                .currentPage(dto.getPage())
                .totalItems(totalRecords)
                .totalPages((int) Math.ceil((double)totalRecords / dto.getPageSize()))
                .status(HttpStatus.OK.value())
                .message("Success")
                .build();
    }

    /**
     * @param param
     * @return
     */
    @Override
    public GlobalReponse intSave(List<CouponDto> param) {

        List<BigDecimal> results = null;
        log.info("*** Coupon, service; save Coupon ***");
        param.stream().forEach(
                item -> {
                    Integer orgId = null;
                    Integer vendorId = null;
                    Integer customerId = null;
                    StringBuilder sql = new StringBuilder("select d_org_id from pos.d_org where erp_org_id = :orgId");
                    Query query = entityManager.createNativeQuery(sql.toString());
                    query.setParameter("orgId", item.getOrgId());

                    if (!query.getResultList().isEmpty()) {
                        orgId = Integer.valueOf(String.valueOf((BigDecimal) query.getSingleResult()));
                    }

                    if (item.getVendorId() != null) {
                        sql = new StringBuilder("select d_vendor_id from pos.d_vendor where erp_vendor_id = :vendorId");
                        query = entityManager.createNativeQuery(sql.toString());
                        query.setParameter("vendorId", item.getVendorId());
                        if (!query.getResultList().isEmpty()) {
                            vendorId = Integer.valueOf(String.valueOf((BigDecimal) query.getSingleResult()));
                        }
                    }
                    if (item.getCustomerId() != null) {
                        sql = new StringBuilder("select d_customer_id from pos.d_customer where erp_customer_id = :customerId");
                        query = entityManager.createNativeQuery(sql.toString());
                        query.setParameter("customerId", item.getCustomerId());
                        if (!query.getResultList().isEmpty()) {
                            customerId = Integer.valueOf(String.valueOf((BigDecimal) query.getSingleResult()));
                        }
                    }
                    if (item.getPosTerminalId() != null) {
                        sql = new StringBuilder("select d_pos_terminal_id from pos.d_pos_terminal where erp_pos_id = :posTerminalId");
                        query = entityManager.createNativeQuery(sql.toString());
                        query.setParameter("posTerminalId", item.getPosTerminalId());
                        if (!query.getResultList().isEmpty()) {
                            item.setPosTerminalId(Integer.valueOf(String.valueOf((BigDecimal) query.getSingleResult())));
                        }

                    }

                    Coupon couponCheck = couponRepository.findByErpCouponIdAndTenantId(item.getErpCouponId(), AuditContext.getAuditInfo().getTenantId()).orElse(null);
                    if (couponCheck == null) {
                        couponCheck = modelMapper.map(item, Coupon.class);
                        couponCheck.setTenantId(AuditContext.getAuditInfo().getTenantId());
                        couponCheck.setOrgId(orgId);
                        couponCheck.setVendorId(vendorId);
                        couponCheck.setCustomerId(customerId);
                        couponRepository.save(couponCheck);
                    } else {
                        modelMapper.map(item, couponCheck);
                        couponCheck.setOrgId(orgId);
                        couponCheck.setVendorId(vendorId);
                        couponCheck.setCustomerId(customerId);
                        couponRepository.save(couponCheck);
                    }
                }
        );
        return GlobalReponse.builder()
                .data(true)
                .message("success")
                .status(HttpStatus.OK.value())
                .errors("").build();
    }
    @Override
    public String updateCouponStatus(String couponCode, String status) {

        log.info("*** Coupon, service; update Coupon status ***");
        try {
            couponRepository.updateStatusByCodeAndTenantId(couponCode, AuditContext.getAuditInfo().getTenantId(), status);
        }catch (Exception e){
            e.printStackTrace();
            return "FAI";
        }

        return "COM";
    }
}
