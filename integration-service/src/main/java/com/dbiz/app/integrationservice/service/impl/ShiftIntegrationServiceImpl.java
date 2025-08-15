package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.ShiftIntegrationService;
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
import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftClosingIntDto;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.integrationDto.posOrder.*;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftControlCredentialInt;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShiftIntegrationServiceImpl implements ShiftIntegrationService {

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

    private final String TOPIC = "sync-shift-control";

    private final ErpIntegrationRepository erpIntegrationRepository;


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
    public void receivedMessage(ConsumerRecord<String, ShiftIntDto> consumerRecord, Acknowledgment acknowledgment) {
        try {

            // Extract key and value from the ConsumerRecord
            String key = consumerRecord.key(); // could be null
            ShiftIntDto value = consumerRecord.value();

            try {
                int tenantNumbers = getTenantNumbers();
                if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
                    Map<Object, Object> configuredDataSources = dataSourceConfigService
                            .configureDataSources();

                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
                }

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

                try {
                    boolean checkIntegration =  entityManager.createNativeQuery("select * from d_erp_integration where d_tenant_id = :tenantId and is_active = 'Y' and erp_platform = 'Idempiere' and is_default = 'Y'", Tuple.class)
                            .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                            .getResultList().size() > 0;
                    if(checkIntegration) {

                        syncIntegrationByKafkaIdempiere(credential, value);
                    } else {

                        syncIntegrationByKafkaErpNext(credential, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }



    public String syncIntegrationByKafkaErpNext(SyncIntegrationCredential credential,
                                                ShiftIntDto order) {

        String statusIntegration = "FAI";
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        try {

            integrationHistoryInfoDto = syncShiftControl(credential, order.getShiftId());
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

    /**
     *
     * @param dto
     * @return
     */
    @Override
    public IntegrationHistoryInfoDto syncShiftIntegration(SyncIntegrationCredential dto) {
        return null;
    }

    /**
     *
     * @param dto
     * @return
     */
    @Override
    public GlobalReponse syncApi(ShiftIntDto dto) {
        SyncIntegrationCredential credential = new SyncIntegrationCredential();
        credential.setOrgId(dto.getOrgId());
        IntegrationHistoryInfoDto response =  this.syncShiftControl(credential, dto.getShiftId());
        return GlobalReponse.builder()
                .data(response)
                .status(HttpStatus.OK.value())
                .message("Success")
                .errors(null)
                .build();
    }


    public IntegrationHistoryInfoDto syncShiftControl(SyncIntegrationCredential dto, Integer posOrderId) {

        TokenERPNextRespDto tokenERPNextRespDto = commonService.getTokenERPNext();
        String statusIntegration = "COM";

        String sql = "select * from d_int_shift_control_v where d_tenant_id = :tenantId and d_org_id = :orgId and d_shift_control_id = :orderId";


        List<Tuple> results = entityManager.createNativeQuery(sql, Tuple.class)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .setParameter("orderId", posOrderId)
                .getResultList();

        List<ShiftClosingIntDto> listInt = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "token " + tokenERPNextRespDto.getApiKey() + ":" + tokenERPNextRespDto.getApiSecret());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> responseErpNext = null;

        for (Tuple row : results) {
            log.info("Row: {}", row);

            ShiftClosingIntDto.BalanceDetailDto balanceDetailDto = ShiftClosingIntDto.BalanceDetailDto.builder()
                    .modeOfPayment("Cash")
                    .openingAmount(ParseHelper.BIGDECIMAL.parse(row.get("start_cash").toString()))
                    .build();
            Instant dateStart = ParseHelper.INSTANT.parse(row.get("start_date"));
            Instant dateEnd =  ParseHelper.INSTANT.parse(row.get("end_date"));
            ShiftClosingIntDto shiftClosingIntDto = ShiftClosingIntDto.builder()
                    .periodStartDate(org.common.dbiz.helper.DateHelper.convertInstantToPattern(dateStart,"yyyy-MM-dd HH:mm:ss"))
                    .postingDate(org.common.dbiz.helper.DateHelper.convertDate(dateStart))
                    .company(row.get("org_code").toString())
                    .posProfile(row.get("pos_terminal_name").toString())
                    .user(row.get("full_name").toString())
                    .periodEndDate(org.common.dbiz.helper.DateHelper.convertInstantToPattern(dateEnd,"yyyy-MM-dd HH:mm:ss"))
                    .postingTime(org.common.dbiz.helper.DateHelper.convertDate(dateEnd))
                    .balanceDetails(List.of(balanceDetailDto))
                    .documentNo(row.get("document_no").toString())
                    .build();
            listInt.add(shiftClosingIntDto);
        }
        ShiftControlCredentialInt listData = null;
        if (!listInt.isEmpty())
            listData = ShiftControlCredentialInt.builder().data(listInt.get(0)).build();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(listData);
            if (listData != null) {
                HttpEntity<ShiftControlCredentialInt > requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenERPNextRespDto.getUrl() +"/api/method/" + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_POINT_INT_SHIFT);
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

    public String syncIntegrationByKafkaIdempiere(SyncIntegrationCredential credential,
                                         ShiftIntDto order) {

        String statusIntegration = "FAI";
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        try {

            integrationHistoryInfoDto = getShiftMainByShiftControl(credential, order.getShiftId());
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

    public IntegrationHistoryInfoDto getShiftMainByShiftControl(SyncIntegrationCredential dto, Integer shiftId) {

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        String statusIntegration = "COM";

        String sql = "SELECT " +
                "dsc.d_shift_control_id, " +
                "dsc.document_no, " +
                "du.full_name, " +
                "dog.code AS org_code, " +
                "dpt.name AS pos_terminal_name, " +
                "dsc.start_date, " +
                "dsc.end_date, " +
                "dsc.start_cash, " +
                "dsc.d_org_id, " +
                "dsc.d_tenant_id, " +
                "dog.erp_org_id, " +
                "dpt.erp_pos_id " +
                "FROM pos.d_shift_control dsc " +
                "JOIN pos.d_user du ON dsc.d_user_id = du.d_user_id " +
                "JOIN pos.d_org dog ON dsc.d_org_id = dog.d_org_id " +
                "JOIN pos.d_pos_terminal dpt ON dsc.d_pos_terminal_id = dpt.d_pos_terminal_id " +
                " WHERE dsc.d_tenant_id = :tenantId " +
                " AND dsc.d_org_id = :orgId and dsc.d_shift_control_id = :shiftId";


        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .setParameter("shiftId", shiftId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        UpdateShiftDto shift = null;
        for (Map<String, Object> row : results) {
            log.info("Row: {}", row);

            shift = UpdateShiftDto.builder()
                    .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .D_Shift_Control_ID(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .sequenceNo(ParseHelper.INT.parse(row.get("d_shift_control_id")))
                    .startDate(row.get("start_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("start_date")))
                            : null)
                    .endDate(row.get("end_date") != null
                            ? DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("end_date")))
                            : null)
                    .shiftType(ParseHelper.STRING.parse(row.get("shift_type")))
                    .build();

        }
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(shift);
            if (shift != null) {
                HttpEntity<UpdateShiftDto> requestEntity = new HttpEntity(shift, headers);

                String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_UDPATE_SHIFT);
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
}
