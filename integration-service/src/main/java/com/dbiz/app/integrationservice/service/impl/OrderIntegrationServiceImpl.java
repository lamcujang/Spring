package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.OrderIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.*;
import org.common.dbiz.dto.integrationDto.posOrder.*;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.UomDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.productRequest.SaveAllProductAttr;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderIntegrationServiceImpl implements OrderIntegrationService {

    private final ModelMapper modelMapper;

    private final CommonIntegrationService commonService;
    private final IntegrationHistoryRepository integrationHistoryRepository;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;


    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    private final String GROUP_ID = "gr-sync-order";

    private final String TOPIC = "sync-order";

    private final ErpIntegrationRepository erpIntegrationRepository;


    @Override
    public String handleOrderIntegration() {
        return "";
    }

    @Override
    public List<PosOrderDto> getPosOrderList() {


        return List.of();
    }

    @Override
    public GlobalReponse syncOrderIntegration(SyncIntegrationCredential dto) {
        log.info("Syncing order integration");
        GlobalReponse globalReponse = new GlobalReponse();

        globalReponse.setStatus(HttpStatus.OK.value());
        globalReponse.setMessage("Syncing order integration");
        globalReponse.setData(getOrders(dto));

        return globalReponse;
    }

    @Override
    public IntegrationHistoryInfoDto syncOrderIntegrationMain(SyncIntegrationCredential dto) {
        return getOrdersMain(dto);
    }

    /**
     *
     * @param posInvoiceId
     * @return
     */
    @Override
    public GlobalReponse syncInvoice(Integer posInvoiceId, Integer orgId) {
        SyncIntegrationCredential credential = new SyncIntegrationCredential();
        credential.setOrgId(orgId);
        IntegrationHistoryInfoDto res = this.getOrdersMainByPosOrderForErpNext(credential, posInvoiceId);
        return GlobalReponse.builder()
                .data(res)
                .status(HttpStatus.OK.value())
                .build();
    }


    public IntegrationHistoryInfoDto getOrdersMain(SyncIntegrationCredential dto) {
        log.info("Get order main");
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        String statusIntegration = "COM";

        String sql = "SELECT d_pos_order_id , " +
                "d_tenant_id, " +
                "d_org_id, " +
                "erp_org_id, " +
                "order_date, " +
                "qrcode_payment,  " +
                "customer_name,  " +
                "phone,  " +
                "flat_amt, " +
                "flat_discount, " +
                "COALESCE(order_guests, 0) as order_guests, " +
                "table_no , " +
                "floor_no , " +
                "d_pricelist_id , " +
                "total_amount , " +
                "erp_pos_id , " +
                "bill_no , " +
                "erp_shift_control_id, " +
                "d_shift_control_id, " +
                "sequence_no, " +
                "shift_type, " +
                "start_date, " +
                "end_date, " +
                "order_status, " +
                "is_sync_erp," +
                "deduction_percent, " +
                "deduction_amount, " +
                "deduction_description " +
                " FROM pos.d_int_pos_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND is_sync_erp = 'N' AND order_status = 'COM'";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CreateOrderDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            CreateOrderDto order = CreateOrderDto.builder()
                    .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .D_Pos_Order_ID(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                    .paymentType("POS Order")
                    .dateOrdered(row.get("order_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date")))
                            : null)
                    .bankName(ParseHelper.STRING.parse(row.get("bankname")))
                    .bankNo(ParseHelper.STRING.parse(row.get("account_no")))
                    .qrCode(ParseHelper.STRING.parse(row.get("qrcode_payment")))
                    .cusName(ParseHelper.STRING.parse(row.get("customer_name")))
                    .cusPhone1(ParseHelper.STRING.parse(row.get("phone")))
                    .flatAmt(ParseHelper.BIGDECIMAL.parse(row.get("flat_amt")))
                    .flatDiscount(ParseHelper.BIGDECIMAL.parse(row.get("flat_discount")))
                    .guest(((Number)row.get("order_guests")).intValue())
                    .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                    .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
//                    .m_PriceList_ID()
                    .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                    .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .orderRef(ParseHelper.STRING.parse(row.get("bill_no"))).build();
            order.setPaymentsInfo(getPayments(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setLinesInfo(getOrderLines(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setDeductionInfo(getDeduction(ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount"))));
            order.setShiftInfo(ShiftInfoDto.builder()
                    .D_Shift_Control_ID(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .sequenceNo(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .startDate(row.get("start_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("start_date")))
                            : null)
                    .endDate(row.get("end_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("end_date")))
                            : null)
                    .shiftType(ParseHelper.STRING.parse(row.get("shift_type"))).build());
            listOrder.add(order);

        }
        ListCreateOrderDto listData = ListCreateOrderDto.builder().data(listOrder).build();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(listData);
            if (listData != null && !listData.getData().isEmpty()) {
                HttpEntity<ListCreateOrderDto> requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_POS_ORDER);
                log.info("url: " + url);
                log.info("payload: " + payload);
                responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (responseIdempiere.getStatusCode().is2xxSuccessful()) {
                    String responseBody = responseIdempiere.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    log.info("response from idempiere: " + jsonNode.toPrettyString());
                    String handleInsertToPos = convertDataFromERPtoPos(jsonNode);
                    log.info("Inserting data to POS: " + handleInsertToPos);
                    return IntegrationHistoryInfoDto.builder()
                            .statusIntegration(handleInsertToPos)
                            .payload(payload)
                            .response(jsonNode.toPrettyString())
                            .build();

                } else {
                    log.info("Loi");
                    statusIntegration = "FAI";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusIntegration = "FAI";
        }


        return IntegrationHistoryInfoDto.builder()
                .statusIntegration(statusIntegration)
                .payload(payload)
                .build();
    }

    @Override
    public List<CreateOrderDto> getOrders(SyncIntegrationCredential dto) {

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        String sql = "SELECT d_pos_order_id , " +
                "d_tenant_id, " +
                "d_org_id, " +
                "erp_org_id, " +
                "order_date, " +
                "qrcode_payment,  " +
                "customer_name,  " +
                "phone,  " +
                "flat_amt, " +
                "flat_discount, " +
                "COALESCE(order_guests, 0) as order_guests, " +
                "table_no , " +
                "floor_no , " +
                "d_pricelist_id , " +
                "total_amount , " +
                "erp_pos_id , " +
                "bill_no , " +
                "erp_shift_control_id, " +
                "d_shift_control_id, " +
                "sequence_no, " +
                "shift_type, " +
                "start_date, " +
                "end_date, " +
                "order_status, " +
                "is_sync_erp, " +
                "deduction_percent, " +
                "deduction_amount, " +
                "deduction_description " +
                " FROM pos.d_int_pos_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND is_sync_erp = 'N' AND order_status = 'COM'";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CreateOrderDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            CreateOrderDto order = CreateOrderDto.builder()
                    .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .D_Pos_Order_ID(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                    .paymentType("POS Order")
                    .dateOrdered(row.get("order_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date")))
                            : null)
                    .bankName(ParseHelper.STRING.parse(row.get("bankname")))
                    .bankNo(ParseHelper.STRING.parse(row.get("account_no")))
                    .qrCode(ParseHelper.STRING.parse(row.get("qrcode_payment")))
                    .cusName(ParseHelper.STRING.parse(row.get("customer_name")))
                    .cusPhone1(ParseHelper.STRING.parse(row.get("phone")))
                    .flatAmt(ParseHelper.BIGDECIMAL.parse(row.get("flat_amt")))
                    .flatDiscount(ParseHelper.BIGDECIMAL.parse(row.get("flat_discount")))
                    .guest(((Number)row.get("order_guests")).intValue())
                    .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                    .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
//                    .m_PriceList_ID()
                    .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                    .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .orderRef(ParseHelper.STRING.parse(row.get("bill_no"))).build();
            order.setPaymentsInfo(getPayments(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setLinesInfo(getOrderLines(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setReceiptOtherInfo(getReceiptOthers(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setDeductionInfo(getDeduction(ParseHelper.BIGDECIMAL.parse(row.get("deduction_amount"))));
            order.setShiftInfo(ShiftInfoDto.builder()
                    .D_Shift_Control_ID(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .sequenceNo(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .startDate(row.get("start_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("start_date")))
                            : null)
                    .endDate(row.get("end_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("end_date")))
                            : null)
                    .shiftType(ParseHelper.STRING.parse(row.get("shift_type"))).build());
            listOrder.add(order);


        }
        ListCreateOrderDto listDate = ListCreateOrderDto.builder().data(listOrder).build();

        try {
            log.info("listData: " + objectMapper.writeValueAsString(listDate));
            HttpEntity<ListCreateOrderDto> requestEntity = new HttpEntity<>(listDate, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_POS_ORDER);
            log.info("url: " + url);
            log.info("requestEntity: " + requestEntity.toString());
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                log.info("response from idempiere: " + jsonNode.toPrettyString());
                log.info("Inserting data to POS: " + convertDataFromERPtoPos(jsonNode));
            } else {
                log.info("Loi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return listOrder;
    }

    @Override
    public IntegrationHistoryInfoDto getOrdersMainByPosOrder(SyncIntegrationCredential dto, Integer posOrderId) {

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        String statusIntegration = "COM";

        String sql = "SELECT d_pos_order_id , " +
                "d_tenant_id, " +
                "d_org_id, " +
                "erp_org_id, " +
                "order_date, " +
                "qrcode_payment,  " +
                "customer_name,  " +
                "phone,  " +
                "flat_amt, " +
                "flat_discount, " +
                "COALESCE(order_guests, 0) as order_guests, " +
                "table_no , " +
                "floor_no , " +
                "d_pricelist_id , " +
                "total_amount , " +
                "erp_pos_id , " +
                "bill_no , " +
                "erp_shift_control_id, " +
                "d_shift_control_id, " +
                "sequence_no, " +
                "shift_type, " +
                "start_date, " +
                "end_date, " +
                "order_status, " +
                "is_sync_erp" +
                " FROM pos.d_int_pos_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND d_pos_order_id = :orderId AND is_sync_erp = 'N' AND order_status = 'COM'";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .setParameter("orderId", posOrderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CreateOrderDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            CreateOrderDto order = CreateOrderDto.builder()
                    .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .D_Pos_Order_ID(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                    .paymentType("POS Order")
                    .dateOrdered(row.get("order_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date")))
                            : null)
                    .bankName(ParseHelper.STRING.parse(row.get("bankname")))
                    .bankNo(ParseHelper.STRING.parse(row.get("account_no")))
                    .qrCode(ParseHelper.STRING.parse(row.get("qrcode_payment")))
                    .cusName(ParseHelper.STRING.parse(row.get("customer_name")))
                    .cusPhone1(ParseHelper.STRING.parse(row.get("phone")))
                    .flatAmt(ParseHelper.BIGDECIMAL.parse(row.get("flat_amt")))
                    .flatDiscount(ParseHelper.BIGDECIMAL.parse(row.get("flat_discount")))
                    .guest(((Number)row.get("order_guests")).intValue())
                    .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                    .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
//                    .m_PriceList_ID()
                    .grandTotal(ParseHelper.BIGDECIMAL.parse(row.get("total_amount")))
                    .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .orderRef(ParseHelper.STRING.parse(row.get("bill_no"))).build();
            order.setPaymentsInfo(getPayments(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setLinesInfo(getOrderLines(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setReceiptOtherInfo(getReceiptOthers(ParseHelper.INT.parse(row.get("d_pos_order_id"))));
            order.setShiftInfo(ShiftInfoDto.builder()
                    .D_Shift_Control_ID(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .sequenceNo(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .startDate(row.get("start_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("start_date")))
                            : null)
                    .endDate(row.get("end_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("end_date")))
                            : null)
                    .shiftType(ParseHelper.STRING.parse(row.get("shift_type"))).build());
            listOrder.add(order);

        }
        ListCreateOrderDto listData = ListCreateOrderDto.builder().data(listOrder).build();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(listData);
            if (listData != null && !listData.getData().isEmpty()) {
                HttpEntity<ListCreateOrderDto> requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_POS_ORDER);
                log.info("url: " + url);
                log.info("payload: " + payload);
                responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (responseIdempiere.getStatusCode().is2xxSuccessful()) {
                    String responseBody = responseIdempiere.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    log.info("response from idempiere: " + jsonNode.toPrettyString());
                    String handleInsertToPos = convertDataFromERPtoPos(jsonNode);
                    log.info("Inserting data to POS: " + handleInsertToPos);
                    return IntegrationHistoryInfoDto.builder()
                            .statusIntegration(handleInsertToPos)
                            .payload(payload)
                            .response(jsonNode.toPrettyString())
                            .build();

                } else {
                    log.info("Loi");
                    statusIntegration = "FAI";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusIntegration = "FAI";
        }


        return IntegrationHistoryInfoDto.builder()
                .statusIntegration(statusIntegration)
                .payload(payload)
                .build();
    }


    public IntegrationHistoryInfoDto getOrdersMainByPosOrderForErpNext(SyncIntegrationCredential dto, Integer posOrderId) {

        TokenERPNextRespDto tokenERPNextRespDto = commonService.getTokenERPNext();
        String statusIntegration = "COM";

        String sql = "SELECT d_pos_order_id , " +
                "d_tenant_id, " +
                "d_org_id, " +
                "erp_org_id, " +
                "order_date, " +
                "qrcode_payment,  " +
                "customer_name,  " +
                "phone,  " +
                "flat_amt, " +
                "flat_discount, " +
                "order_guests, " +
                "table_no , " +
                "floor_no , " +
                "d_pricelist_id , " +
                "total_amount , " +
                "erp_pos_id , " +
                "bill_no , " +
                "erp_shift_control_id, " +
                "d_shift_control_id, " +
                "sequence_no, " +
                "shift_type, " +
                "start_date, " +
                "end_date, " +
                "order_status, " +
                "is_sync_erp , org_code,org_address,ptm_name,pricelist_name,currency_code,customer_code,document_no,warehouse_code,full_name " +
                " FROM pos.d_int_pos_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND d_pos_order_id = :orderId AND is_sync_erp = 'N' AND order_status = 'COM'";


        List<Tuple> results = entityManager.createNativeQuery(sql, Tuple.class)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .setParameter("orderId", posOrderId)
                .getResultList();

        List<PosInvoiceErpNextDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "token " + tokenERPNextRespDto.getApiKey() + ":" + tokenERPNextRespDto.getApiSecret());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> responseErpNext = null;

        for (Tuple row : results) {
            log.info("Row: {}", row);

            PosInvoiceErpNextDto posInvoice = PosInvoiceErpNextDto.builder()
                    .docstatus(1)
                    .documentNo(row.get("document_no").toString())
                    .title(row.get("customer_code").toString())
                    .customer(row.get("customer_code").toString())
                    .posProfile(row.get("ptm_name").toString())
                    .isPos(1)
                    .isReturn(0)
                    .updateStock(1)
                    .updateBilledAmountInSalesOrder(0)
                    .updateBilledAmountInDeliveryNote(1)
                    .company(row.get("org_code").toString())
                    .postingDate(DateHelper.fromInstant(ParseHelper.INSTANT.parse(row.get("order_date"))))
                    .setPostingTime(0)
                    .dueDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date"))))
                    .accountDate(DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("order_date"))))
                    .companyAddress(row.get("org_address").toString())
                    .currency(row.get("currency_code").toString())
                    .sellingPriceList(row.get("pricelist_name").toString())
                    .priceListCurrency(row.get("currency_code").toString())
                    .updateStock(1)
                    .groupSameItems(0)
                    .language("en")
                    .isDiscounted(0)
                    .status("Paid")
                    .partyAccountCurrency(row.get("currency_code").toString())
                    .isOpening("No")
                    .remarks("No Remarks")
                    .dPosOrderId(row.get("d_pos_order_id").toString())
                    .setWarehouse(row.get("warehouse_code").toString())
                    .salesRepresentative(row.get("full_name").toString())
                    .description("").build();
            String accountForChangeAmount = entityManager.createNativeQuery("select value from d_config where name = 'INT_ACCOUNT_FOR_CHANGE_AMOUNT' and d_tenant_id = :tenantId")
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getSingleResult().toString();
            String debitTo = entityManager.createNativeQuery("select value from d_config where name = 'INT_DEBIT_TO' and d_tenant_id = :tenantId")
                    .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                    .getSingleResult().toString();
            posInvoice.setAccountForChangeAmount(accountForChangeAmount);
            posInvoice.setDebitTo(debitTo);
            posInvoice.setPayments(getPaymentsErpNext(ParseHelper.INT.parse(String.valueOf(row.get("d_pos_order_id")))));
            posInvoice.setItems(getOrderLinesErpNext(ParseHelper.INT.parse(String.valueOf(row.get("d_pos_order_id")))));
            posInvoice.setTaxes(getTaxesErpNext(ParseHelper.INT.parse(String.valueOf(row.get("d_pos_order_id")))));
            listOrder.add(posInvoice);

        }
        PosInvoiceErpNextIntDto listData = null;
        if (!listOrder.isEmpty())
            listData = PosInvoiceErpNextIntDto.builder().data(listOrder.get(0)).build();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(listData);
            if (listData != null) {
                HttpEntity<PosInvoiceErpNextIntDto> requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenERPNextRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_RESOURCE) + "/" + "POS Invoice";
                log.info("url: " + url);
                log.info("payload: " + payload);
                responseErpNext = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                    String responseBody = responseErpNext.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    log.info("response from erpnext: " + jsonNode.toPrettyString());
                    String handleInsertToPos = convertDataFromERPtoPos(jsonNode);
                    log.info("Inserting data to POS: " + handleInsertToPos);
                    return IntegrationHistoryInfoDto.builder()
                            .statusIntegration(handleInsertToPos)
                            .payload(payload)
                            .response(jsonNode.toPrettyString())
                            .build();

                } else {
                    log.info("Loi");
                    statusIntegration = "FAI";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusIntegration = "FAI";
        }


        return IntegrationHistoryInfoDto.builder()
                .statusIntegration(statusIntegration)
                .payload(payload)
                .build();
    }


    public List<PaymentInfoDto> getPayments(Integer orderId) {

        String sql = "SELECT payment_method , " +
                "code, " +
                "amt, " +
                "transaction_id " +
                " FROM pos.d_pos_payment_v WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PaymentInfoDto> listPayment = new ArrayList<>();
        List<CreateCouponDto> listCreateCoupon = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            PaymentInfoDto payment = PaymentInfoDto.builder()
                    .paymentRule(ParseHelper.STRING.parse(row.get("payment_method")))
                    .voucherNo(ParseHelper.STRING.parse(row.get("code")))
                    .paymentAmt(ParseHelper.BIGDECIMAL.parse(row.get("amt")))
                    .transID(ParseHelper.STRING.parse(row.get("transaction_id"))).build();

            // kiem tra xem coupon da co tren erp chua
            try{
                if(payment.getPaymentRule().equals("COU") && payment.getVoucherNo() != null && !payment.getVoucherNo().isEmpty())
                {
                    String sqlCheckCoupon = "select erp_coupon_id,d_coupon_id, coalesce(do2.erp_org_id, 0) as d_org_id,balance_amount,\n" +
                            "       coalesce(dpt.erp_pos_id,0)   as d_pos_terminal_id, dp.is_active , dp.is_available,\n" +
                            "      coalesce(dv.erp_vendor_id ,0)  as d_vendor_id,\n" +
                            "      coalesce(dc.erp_customer_id,0) as d_customer_id\n" +
                            "from d_coupon dp left join d_pos_terminal dpt on dp.d_pos_terminal_id = dpt.d_pos_terminal_id\n" +
                            "left join  d_vendor dv on dp.d_vendor_id = dv.d_vendor_id\n" +
                            "left join d_customer dc on dc.d_customer_id = dp.d_customer_id\n" +
                            "left join d_org do2 on do2.d_org_id = dp.d_org_id where dp.code = :code\n ";
                    List<Map<String, Object>> resultsCoupon = entityManager.createNativeQuery(sqlCheckCoupon)
                            .setParameter("code",payment.getVoucherNo())
                            .unwrap(NativeQuery.class)
                            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                            .getResultList();
                    for (Map<String, Object> coupon : resultsCoupon) {
                        log.info("Row: {}", coupon);
                        if(ParseHelper.INT.parse(coupon.get("erp_coupon_id")) != null && (Integer) ParseHelper.INT.parse(coupon.get("erp_coupon_id")) > 0)
                            continue; // da co coupon tren erp roi
                        CreateCouponDto couponDto = CreateCouponDto.builder()
                                .couponAmt(ParseHelper.BIGDECIMAL.parse(coupon.get("balance_amount")))
                                .couponCode(payment.getVoucherNo())
                                .C_BPartner_ID(ParseHelper.INT.parse(coupon.get("d_vendor_id")))
                                .ad_Org_ID(ParseHelper.INT.parse(coupon.get("d_org_id")))
                                .C_POS_ID(ParseHelper.INT.parse(coupon.get("d_pos_terminal_id")))
                                .isActive(ParseHelper.STRING.parse(coupon.get("is_active")))
                                .isAvailable(ParseHelper.STRING.parse(coupon.get("is_available")))
                                .C_BPartner2_ID(ParseHelper.INT.parse(coupon.get("d_customer_id")))
                                .build();
                        listCreateCoupon.add(couponDto);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
                log.error("Error while checking coupon: " + e.getMessage());
            }
            listPayment.add(payment);
        }
        // dong bo coupon ve erp
        try{
            TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenIdempiereRespDto.getToken());
            ResponseEntity<String> responseIdempiere = null;

            ListCreateCouponDto payloadCreateCoupon = ListCreateCouponDto.builder().data(listCreateCoupon).build();
            log.info("listData: " + objectMapper.writeValueAsString(listCreateCoupon));
            HttpEntity<ListCreateCouponDto> requestEntity = new HttpEntity<>(payloadCreateCoupon, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_COUPON);
            log.info("url: " + url);
            log.info("requestEntity: " + requestEntity.toString());
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                log.info("response from idempiere: " + jsonNode.toPrettyString());
                log.info("Inserting data to POS: " + convertDataFromERPtoPos(jsonNode));
            } else {
                log.info("Loi");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return listPayment;
    }

    public List<PosInvoiceErpNextDto.PaymentDto> getPaymentsErpNext(Integer orderId) {

        String sql = "SELECT payment_method , " +
                "code, " +
                "amt, " +
                "transaction_id " +
                " FROM pos.d_pos_payment_v WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PosInvoiceErpNextDto.PaymentDto> listPayment = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);
            StringBuilder paymentType = null;
            if (row.get("payment_method").equals("CAS"))
                paymentType = new StringBuilder("Cash");
            else if (row.get("payment_method").equals("BAN") ||row.get("payment_method").equals("QRC")  )
                paymentType = new StringBuilder("Wire Transfer");
            else if (row.get("payment_method").equals("CHE"))
                paymentType = new StringBuilder("Cheque");
            else if (row.get("payment_method").equals("DEB"))
                paymentType = new StringBuilder("Credit Card");

            PosInvoiceErpNextDto.PaymentDto payment = PosInvoiceErpNextDto.PaymentDto.builder()
                    .modeOfPayment(paymentType.toString())
                    .amount(ParseHelper.BIGDECIMAL.parse(row.get("amt")))
                    .baseAmount(ParseHelper.BIGDECIMAL.parse(row.get("amt"))).build();
            listPayment.add(payment);

        }

        return listPayment;
    }


    public List<LineInfoDto>  getOrderLines(Integer orderId) {
        //neu product chua dong bo trong erp thi tien hanh tao
        createProductInErpAndUpdateProduct(orderId);

        String sql = "SELECT salesprice, " +
                "qty," +
                "d_tax_id ," +
                "erp_tax_id," +
                "tax_amount ," +
                "d_product_id ," +
                "erp_product_id," +
                "d_pos_order_id,price_actual " +
                " FROM pos.d_int_pos_orderline_v WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<LineInfoDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            LineInfoDto line = LineInfoDto.builder()
                    .priceActual(ParseHelper.BIGDECIMAL.parse(row.get("price_actual")))
                    .qtyOrdered(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                    .priceBeforeDiscount(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                    .taxID(ParseHelper.INT.parse(row.get("erp_tax_id")))
                    .taxAmt(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount")))
                    .m_Product_ID(ParseHelper.INT.parse(row.get("erp_product_id"))).build();
            listOrderLine.add(line);
        }

        return listOrderLine;
    }

    //Tao product trong ERP
    public void createProductInErpAndUpdateProduct(Integer orderId){
        log.info("create product InErp");

        String sql = "SELECT * " +
                 "FROM \n" +
                "    pos.d_int_pos_orderline_v_2 \n" +
                "WHERE \n" +
                "    d_tenant_id = :tenantId \n" +
                "    AND d_pos_order_id = :orderId " +
                "    AND erp_product_id is null ";

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ProductErpDto> data = new ArrayList<>();
        for(Map<String, Object> row: results){
            Integer productId = ParseHelper.INT.parse(row.get("d_product_id"));
            String isStocked = ParseHelper.STRING.parse(row.get("is_stocked"));
            ProductErpDto productErpDto = ProductErpDto.builder()
                    .value(ParseHelper.STRING.parse(row.get("product_code")))
                    .name(ParseHelper.STRING.parse(row.get("product_name")))
                    .productType("l")
                    .mProductCataegoryId(ParseHelper.INT.parse(row.get("erp_product_category_id")))
                    .salesPrice(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                    .taxCategoryId(ParseHelper.INT.parse(row.get("erp_tax_id")))
                    .unShowPos(0)
//                    .ad_Client_ID(null)
                    .isStocked(isStocked)
                    .description(ParseHelper.STRING.parse(row.get("product_description")))
                    .posTerminalId(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .productId(productId)
                    .uom(UomErpDto.builder()
                            .uomId(ParseHelper.INT.parse(row.get("d_uom_id")))
                            .c_Uom_ID(ParseHelper.INT.parse(row.get("erp_uom_id")))
                            .code(ParseHelper.STRING.parse(row.get("uom_code")))
                            .name(ParseHelper.STRING.parse(row.get("uom_name")))
                            .build())
                    .listOrgIds(getOrgProductAcc(productId))
                    .build();

            if("Y".equals(isStocked)){
                productErpDto.setProductLocations(getProductLocation(productId));
            }

            List<ProductComponentErpDto> productCombos = getProductCombo(productId);

            productErpDto.setProductBoms(productCombos.stream()
                    .filter(dto -> BigDecimal.ZERO.compareTo(dto.getQty()) != 0)
                    .collect(Collectors.toList()));

            List<ProductComponentErpDto> listQtyZero = productCombos.stream()
                    .filter(dto -> BigDecimal.ZERO.compareTo(dto.getQty()) == 0)
                    .collect(Collectors.toList());
            if(!listQtyZero.isEmpty()){
                productErpDto.setProductExtra(listQtyZero);
                productErpDto.setIsAuxilira(1);// Co mon them
            }else{
                productErpDto.setIsAuxilira(0);
            }

            log.info("Dto ProductErp: {}", productErpDto);
            data.add(productErpDto);
        }

        if(!data.isEmpty()) createProductErp(data);
    }

    private void createProductErp(List<ProductErpDto> productErpDto){
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;


        try {
            log.info("listData: " + objectMapper.writeValueAsString(productErpDto));
            ProductErpRequestWrapper productErpRequestWrapper = new ProductErpRequestWrapper();
            productErpRequestWrapper.setData(productErpDto);
            HttpEntity<ProductErpRequestWrapper> requestEntity = new HttpEntity<>(productErpRequestWrapper, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_CREATE_PRODUCT);
            log.info("url: " + url);
            log.info("requestEntity: " + requestEntity.toString());
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                log.info("response from idempiere: " + jsonNode.toPrettyString());
                log.info("Inserting data to POS: " + convertDataFromERPtoPos(jsonNode));

                JsonNode dataNode = jsonNode.get("data");
                if (dataNode != null && dataNode.isArray()) {
                    SaveAllProductAttr saveAllProductAttr = new SaveAllProductAttr();
                    List<ProductDto> productDtos = new ArrayList<>();
                    for (JsonNode item : dataNode) {
                        ProductDto dto = ProductDto.builder()
                                .erpProductId(item.has("M_Product_ID") ? item.get("M_Product_ID").asInt() : null)
                                .id(item.has("d_product_id") ? item.get("d_product_id").asInt() : null)
                                .uom(UomDto.builder()
                                        .id(item.has("d_Uom_ID") ? item.get("d_Uom_ID").asInt() : null)
                                        .erpUomId(item.has("C_UOM_ID") ? item.get("C_UOM_ID").asInt() : null)
                                        .build())
                                .build();
                        productDtos.add(dto);
                    }
                    saveAllProductAttr.setListProductDto(productDtos);
                    saveAllProductAttr.setItemProducts(new ArrayList<>());
                    saveAllProductAttr.setToppingIds(new Integer[0]);

                    log.info("dto : {}", saveAllProductAttr);

                    log.info("update product");
                    updateProduct(saveAllProductAttr);
                }

            } else {
                log.info("Loi");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(SaveAllProductAttr saveAllProductAttr){
        if(saveAllProductAttr != null ) {
            log.info("MaintenantID : {}", AuditContext.getAuditInfo().getMainTenantId().toString());
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SaveAllProductAttr> requestEntity = new HttpEntity<>(saveAllProductAttr, headers);

            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.   PRODUCT_SERVICE_API_UPDATE_PRODUCT, requestEntity, GlobalReponse.class);

            log.info("Update: {}", response.getStatus().toString());
        }
    }

    //Lay danh sach assign org cua product
    private List<Integer> getOrgProductAcc(Integer productId){
        String sql = "select erp_org_id from d_assign_org_product dao join d_org do2 on dao.d_org_id = do2.d_org_id  " +
                "where d_product_id = :productId\n";

        List<Object> rawResults = entityManager.createNativeQuery(sql)
                .setParameter("productId", productId)
                .getResultList();

        return rawResults.stream()
                .map(result -> ((Number) result).intValue())
                .collect(Collectors.toList());
    }

    //Lay danh sach thanh phan va mon them
    private List<ProductComponentErpDto> getProductCombo(Integer productId){
        String sql = "select dpc.d_product_component_id , erp_product_id ,dpc.is_item, dpc.qty " +
                "from d_product_combo dpc join d_product dp \n" +
                "on dpc.d_product_component_id  = dp.d_product_id" +
                "  where dpc.d_product_id = :productId";

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("productId", productId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ProductComponentErpDto> data = new ArrayList<>();
        for (Map<String, Object> row : results) {
            String is_item = ParseHelper.STRING.parse(row.get("is_item"));
            BigDecimal qty =("Y".equals(is_item)) ? ParseHelper.BIGDECIMAL.parse(row.get("qty")) : BigDecimal.ZERO;
            ProductComponentErpDto productLocationErpDto = ProductComponentErpDto.builder()
                    .componentId(ParseHelper.INT.parse(row.get("erp_product_id")))
                    .qty(qty)
                    .build();
            data.add(productLocationErpDto);
        }

        return data;
    }

    //Lay danh sach assign org cua product
    private List<ProductLocationErpDto> getProductLocation(Integer productId){
        String sql = "select dpl.d_warehouse_id , erp_warehouse_id, dor.erp_org_id  \n" +
                "from d_product_location dpl \n" +
                "join d_warehouse dw on dw.d_warehouse_id = dpl.d_warehouse_id  \n" +
                "join d_org dor on dpl.d_org_id = dor.d_org_id \n" +
                "where d_product_id = :productId";

        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("productId", productId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<ProductLocationErpDto> data = new ArrayList<>();
        for (Map<String, Object> row : results) {
            ProductLocationErpDto productLocationErpDto = ProductLocationErpDto.builder()
                    .mWarehouseId(ParseHelper.INT.parse(row.get("erp_warehouse_id")))
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .build();
            data.add(productLocationErpDto);
        }

        return data;
    }


    public List<PosInvoiceErpNextDto.ItemDto> getOrderLinesErpNext(Integer orderId) {

        String incomeAccount = entityManager.createNativeQuery("select value from d_config where name = 'INT_INCOME_ACCOUNT' and d_tenant_id = :tenantId")
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult().toString();

        String expenseAccount = entityManager.createNativeQuery("select value from d_config where name = 'INT_EXPENSE_ACCOUNT' and d_tenant_id = :tenantId")
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult().toString();

        String sql = "SELECT salesprice, " +
                "qty," +
                "d_tax_id ," +
                "erp_tax_id," +
                "tax_amount ," +
                "d_product_id ," +
                "erp_product_id," +
                "d_pos_order_id,price_actual,product_code,tax_name,description " +
                " FROM pos.d_int_pos_orderline_v WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId ";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<PosInvoiceErpNextDto.ItemDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            PosInvoiceErpNextDto.ItemDto line = PosInvoiceErpNextDto.ItemDto.builder()
                    .identifier(ParseHelper.STRING.parse(row.get("product_code")))
                    .itemCode(ParseHelper.STRING.parse(row.get("product_code")))
                    .itemName(ParseHelper.STRING.parse(row.get("product_code")))
                    .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                    .rate(ParseHelper.BIGDECIMAL.parse(row.get("salesprice")))
                    .itemTaxTemplate(ParseHelper.STRING.parse(row.get("tax_name")))
                    .description((row.get("description") != null && !row.get("description").toString().isEmpty()) ? ParseHelper.STRING.parse(row.get("description")) : ParseHelper.STRING.parse(row.get("product_code")))
                    .incomeAccount(incomeAccount)
                    .build();
            listOrderLine.add(line);
        }

        return listOrderLine;
    }

    public List<PosInvoiceErpNextDto.TaxDto> getTaxesErpNext(Integer orderId) {
        List<PosInvoiceErpNextDto.TaxDto> listTax = new ArrayList<>();
        String chargeType = entityManager.createNativeQuery("select value from d_config where name = 'INT_CHARGE_TYPE' and d_tenant_id = :tenantId")
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult().toString();
        String accountHead = entityManager.createNativeQuery("select value from d_config where name = 'INT_ACCOUNT_HEAD' and d_tenant_id = :tenantId")
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .getSingleResult().toString();
        PosInvoiceErpNextDto.TaxDto tax = PosInvoiceErpNextDto.TaxDto.builder()
                .chargeType(chargeType)
                .accountHead(accountHead)
                .description(chargeType)
                .build();
        listTax.add(tax);
        return listTax;
    }

    public List<LineInfoDto> getReceiptOthers(Integer orderId) {

        String sql = "SELECT receipt_amount, " +
                "qty," +
                "d_tax_id ," +
                "erp_tax_id," +
                "tax_amount ," +
                "d_pos_order_id " +
                " FROM pos.d_int_pos_receipt_other_v WHERE d_tenant_id = :tenantId " +
                " AND d_pos_order_id = :orderId and is_cal= 'Y'";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<LineInfoDto> listOrderLine = new ArrayList<>();
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            LineInfoDto line = LineInfoDto.builder()
                    .priceActual(ParseHelper.BIGDECIMAL.parse(row.get("receipt_amount")))
                    .qtyOrdered(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                    .priceBeforeDiscount(ParseHelper.BIGDECIMAL.parse(row.get("receipt_amount")))
                    .taxID(ParseHelper.INT.parse(row.get("erp_tax_id")))
                    .taxAmt(ParseHelper.BIGDECIMAL.parse(row.get("tax_amount"))).build();
            listOrderLine.add(line);
        }

        return listOrderLine;
    }

    public String convertDataFromERPtoPos(JsonNode jsonParentNode) {

        log.info("Convert data from ERP to POS");
        log.info("jsonNode: " + jsonParentNode.toPrettyString());
        List<PosOrderDto> posOrderDtoList = new ArrayList<>();
        for (JsonNode node : jsonParentNode.path("data")) {

            if (node.path("success").asBoolean() == true) {
                log.info("Success");
                if (node.path("data") != null && !node.path("data").isEmpty()) {
                    PosOrderDto posOrderDto = PosOrderDto.builder()
                            .id(node.path("data").path("D_Pos_Order_ID").asInt())
                            .erpPosOrderId(node.path("data").path("c_Order_ID").asInt())
                            .erpPosOrderNo(node.path("data").path("DocumentNo").asText())
                            .isSyncErp("Y")
                            .build();
                    posOrderDtoList.add(posOrderDto);
                }

            }

        }

        if (!posOrderDtoList.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<PosOrderDto>> requestEntity = new HttpEntity<>(posOrderDtoList, headers);

            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_INTEGRATION_POS_ORDER, requestEntity, GlobalReponse.class);

            if (response.getStatus() != HttpStatus.OK.value()) {
                return "FAI";
            }
            return "COM";
        }

        return "FAI";
    }

    @KafkaListener(groupId = GROUP_ID, topics = TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessage(ConsumerRecord<String, SyncOrderInfoDto> consumerRecord, Acknowledgment acknowledgment) {
        try {

            // Extract key and value from the ConsumerRecord
            String key = consumerRecord.key(); // could be null
            SyncOrderInfoDto value = consumerRecord.value();

            try {
                int tenantNumbers = getTenantNumbers();
                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                    Map<Object, Object> configuredDataSources = dataSourceConfigService
                            .configureDataSources();

                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
                }
//                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
//                    Map<Object, Object> configuredDataSources = dataSourceConfigService
//                            .configureDataSources();
//
//                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (value.getTenantId() != 0) {
                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
            } else {
                dataSourceContextHolder.setCurrentTenantId(null);

            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "en", value.getTenantId()));
            ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
            if (erpIntegration != null) {
                log.info("Received message:");
                log.info("Key: " + key);
                log.info("Value: " + value);
                SyncIntegrationCredential credential = new SyncIntegrationCredential();
                credential.setOrgId(value.getOrgId());
                credential.setDataType("SOR");
                credential.setIntFlow("PTE");
//            credential.setIntType("INSERT");
//            credential.setFromType(1);
                if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE))
                    syncIntegrationByKafka(credential, value);
                else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT))
                    syncIntegrationByKafkaErpNext(credential, value);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    public String syncIntegrationByKafka(SyncIntegrationCredential credential,
                                         SyncOrderInfoDto order) {

        String statusIntegration = "FAI";
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        try {

            integrationHistoryInfoDto = getOrdersMainByPosOrder(credential, order.getPosOrderId());
            statusIntegration = integrationHistoryInfoDto.getStatusIntegration();
            IntegrationHistory integrationHistory = null;
            integrationHistory = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .intStatus(statusIntegration)
                    .userId(order.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(order.getOrgId())
                    .payload(integrationHistoryInfoDto.getPayload())
                    .response(integrationHistoryInfoDto.getResponse())
                    .build();
            integrationHistoryRepository.save(integrationHistory);

        } catch (Exception e) {
            statusIntegration = "FAI";
            IntegrationHistory integrationHistory = null;
            integrationHistory = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .intStatus(statusIntegration)
                    .userId(order.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(order.getOrgId())
                    .description(e.getMessage())
                    .payload(integrationHistoryInfoDto != null
                            ? integrationHistoryInfoDto.getPayload() : null)
                    .response(integrationHistoryInfoDto != null ? integrationHistoryInfoDto.getResponse() : null)
                    .build();
            integrationHistoryRepository.save(integrationHistory);
        }
        return statusIntegration;
    }

    public String syncIntegrationByKafkaErpNext(SyncIntegrationCredential credential,
                                                SyncOrderInfoDto order) {

        String statusIntegration = "FAI";
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        try {

            integrationHistoryInfoDto = getOrdersMainByPosOrderForErpNext(credential, order.getPosOrderId());
            statusIntegration = integrationHistoryInfoDto.getStatusIntegration();
            IntegrationHistory integrationHistory = null;
            integrationHistory = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .intStatus(statusIntegration)
                    .userId(order.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(order.getOrgId())
                    .payload(integrationHistoryInfoDto.getPayload())
                    .response(integrationHistoryInfoDto.getResponse())
                    .build();
            integrationHistoryRepository.save(integrationHistory);

        } catch (Exception e) {
            statusIntegration = "FAI";
            IntegrationHistory integrationHistory = null;
            integrationHistory = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .intStatus(statusIntegration)
                    .userId(order.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(order.getOrgId())
                    .description(e.getMessage())
                    .payload(integrationHistoryInfoDto != null
                            ? integrationHistoryInfoDto.getPayload() : null)
                    .response(integrationHistoryInfoDto != null ? integrationHistoryInfoDto.getResponse() : null)
                    .build();
            integrationHistoryRepository.save(integrationHistory);
        }
        return statusIntegration;
    }

    public List<LineInfoDto> getDeduction(BigDecimal amount) {
        String sql1 = "SELECT d_tax_id  FROM pos.d_tax where tax_rate = 0 LIMIT 1;";

        Integer taxId = ((Number) entityManager.createNativeQuery(sql1)
                .getSingleResult()).intValue();

        List<LineInfoDto> listOrderLine = new ArrayList<>();
        if(amount != null){
            LineInfoDto deductionDto = LineInfoDto.builder()
                    .priceActual(amount.negate())
                    .qtyOrdered(BigDecimal.ONE)
                    .priceBeforeDiscount(amount.negate())
                    .taxID(taxId)
                    .taxAmt(BigDecimal.ZERO)
                    .build();
            listOrderLine.add(deductionDto);
        }

        return listOrderLine;
    }
}
