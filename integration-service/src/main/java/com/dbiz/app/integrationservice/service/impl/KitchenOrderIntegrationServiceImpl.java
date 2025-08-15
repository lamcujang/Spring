package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.KitchenOrderIntegrationService;
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
import org.common.dbiz.dto.integrationDto.KitchenOrder.CreateKitchenOrderDto;
import org.common.dbiz.dto.integrationDto.KitchenOrder.KitchenOrderLineDto;
import org.common.dbiz.dto.integrationDto.KitchenOrder.ListCreateKitchenOrderDto;
import org.common.dbiz.dto.integrationDto.SyncOrderInfoDto;
import org.common.dbiz.dto.integrationDto.posOrder.CreateOrderDto;
import org.common.dbiz.dto.integrationDto.posOrder.LineInfoDto;
import org.common.dbiz.dto.integrationDto.posOrder.ListCreateOrderDto;
import org.common.dbiz.dto.integrationDto.posOrder.ShiftInfoDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.orderDto.KitchenOrderDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class KitchenOrderIntegrationServiceImpl implements KitchenOrderIntegrationService {

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

    private final ObjectMapper objectMapper;

    private final ErpIntegrationRepository erpIntegrationRepository;
    private final EntityManager entityManager;
    private final String GROUP_ID = "gr-sync-order";

    private final String TOPIC  = "sync-kitchen-order";


    @Override
    public IntegrationHistoryInfoDto syncKitchenOrderIntegrationMain(SyncIntegrationCredential dto) {

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        String statusIntegration = "COM";

        String sql = "SELECT d_kitchen_order_id , " +
                "erp_org_id, " +
                "erp_user_id, " +
                "erp_warehouse_id, " +
                "erp_pos_id,  " +
                "floor_no,  " +
                "table_no,  " +
                "order_status, " +
                "dateordered,d_pos_order_id " +
                " FROM pos.d_int_kitchen_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND is_sync_erp = 'N' AND is_active = 'Y' and order_status = 'PAI' ";



        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<CreateKitchenOrderDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        for ( Map<String, Object> row : results ) {
            log.info("Row: {}", row);

            CreateKitchenOrderDto kitchenOrderDto = CreateKitchenOrderDto.builder()
                            .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                            .ad_User_ID(ParseHelper.INT.parse(row.get("erp_user_id")))
                            .d_KitchenOrderID(ParseHelper.INT.parse(row.get("d_kitchen_order_id")))
                            .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                            .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
                            .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                            .m_Warehouse_ID(ParseHelper.INT.parse(row.get("erp_warehouse_id")))
                            .orderStatus(ParseHelper.STRING.parse(row.get("order_status")))
                            .d_PosOrderID(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                            .orderDate(row.get("dateordered") != null
                                ? DateHelper.fromInstant(ParseHelper.INSTANT.parse(row.get("dateordered"))) : null)
                            .lines(getKitchenOrderLines(ParseHelper.INT.parse(row.get("d_kitchen_order_id"))))
                            .build();


            listOrder.add(kitchenOrderDto);

        }
        ListCreateKitchenOrderDto listData = ListCreateKitchenOrderDto.builder().data(listOrder).build();
        String payload = "";
        try{
            payload = objectMapper.writeValueAsString(listData);
            if(listData != null && !listData.getData().isEmpty()){
                HttpEntity<ListCreateKitchenOrderDto> requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_KITCHEN_ORDER);
                log.info("url: " + url);
                log.info("payload: " + payload);
                responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                    String responseBody = responseIdempiere.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    log.info("response from idempiere: " + jsonNode.toPrettyString());
                    String handleInsertToPos = convertDataFromERPtoPos(jsonNode);
                    log.info("Inserting data to POS: " +handleInsertToPos);
                    return IntegrationHistoryInfoDto.builder()
                            .statusIntegration(handleInsertToPos)
                            .payload(payload)
                            .response(jsonNode.toPrettyString())
                            .build();

                }else{
                    log.info("Loi");
                    statusIntegration = "FAI";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            statusIntegration = "FAI";
        }


        return IntegrationHistoryInfoDto.builder()
                .statusIntegration(statusIntegration)
                .payload(payload)
                .build();
    }

    public IntegrationHistoryInfoDto syncKitchenOrderIntegrationMainByOrder(SyncIntegrationCredential dto,Integer orderId) {
        log.info("Sync Kitchen Order Integration Main By Order");

        String sqlCheck = "select orderline_status from d_kitchen_orderline where d_kitchen_order_id = :kitchenOrderId";
        List<String> orderLineStatus = entityManager.createNativeQuery(sqlCheck)
                .setParameter("kitchenOrderId", orderId)
                .unwrap(NativeQuery.class)
                .addScalar("orderline_status", StandardBasicTypes.STRING)
                .getResultList();
        if(orderLineStatus != null && !orderLineStatus.isEmpty()){
            log.info("orderLineStatus: " + orderLineStatus);
        }

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        String statusIntegration = "COM";

        String sql = "SELECT d_kitchen_order_id , " +
                "erp_org_id, " +
                "erp_user_id, " +
                "erp_warehouse_id, " +
                "erp_pos_id,  " +
                "floor_no,  " +
                "table_no,  " +
                "order_status, " +
                "dateordered,d_pos_order_id " +
                " FROM pos.d_int_kitchen_order_v WHERE d_tenant_id = :tenantId " +
                " AND d_org_id = :orgId AND d_kitchen_order_id = :orderId " +
                " AND is_sync_erp = 'N' AND is_active = 'Y' and order_status = 'PAI' ";



        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orgId", dto.getOrgId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        log.info("param: ");
        log.info("tenantId: " + AuditContext.getAuditInfo().getTenantId());
        log.info("orgId: " + dto.getOrgId());
        log.info("orderId: " + orderId);
        List<CreateKitchenOrderDto> listOrder = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;

        for ( Map<String, Object> row : results ) {
            log.info("Row: {}", row);

            CreateKitchenOrderDto kitchenOrderDto = CreateKitchenOrderDto.builder()
                    .ad_Org_ID(ParseHelper.INT.parse(row.get("erp_org_id")))
                    .ad_User_ID(ParseHelper.INT.parse(row.get("erp_user_id")))
                    .d_KitchenOrderID(ParseHelper.INT.parse(row.get("d_kitchen_order_id")))
                    .floorNo(ParseHelper.STRING.parse(row.get("floor_no")))
                    .tableNo(ParseHelper.STRING.parse(row.get("table_no")))
                    .terminalID(ParseHelper.INT.parse(row.get("erp_pos_id")))
                    .m_Warehouse_ID(ParseHelper.INT.parse(row.get("erp_warehouse_id")))
                    .orderStatus(ParseHelper.STRING.parse(row.get("order_status")))
                    .d_PosOrderID(ParseHelper.INT.parse(row.get("d_pos_order_id")))
                    .orderDate(row.get("dateordered") != null
                            ? DateHelper.fromInstant(ParseHelper.INSTANT.parse(row.get("dateordered"))) : null)
                    .lines(getKitchenOrderLines(ParseHelper.INT.parse(row.get("d_kitchen_order_id"))))
                    .build();


            listOrder.add(kitchenOrderDto);

        }
        ListCreateKitchenOrderDto listData = ListCreateKitchenOrderDto.builder().data(listOrder).build();
        String payload = "";
        try{
            payload = objectMapper.writeValueAsString(listData);
            if(listData != null && !listData.getData().isEmpty()){
                HttpEntity<ListCreateKitchenOrderDto> requestEntity = new HttpEntity<>(listData, headers);

                String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_INT_CREATE_KITCHEN_ORDER);
                log.info("url: " + url);
                log.info("payload: " + payload);
                responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                    String responseBody = responseIdempiere.getBody();
                    JsonNode jsonNode = objectMapper.readTree(responseBody);
                    log.info("response from idempiere: " + jsonNode.toPrettyString());
                    String handleInsertToPos = convertDataFromERPtoPos(jsonNode);
                    log.info("Inserting data to POS: " +handleInsertToPos);
                    return IntegrationHistoryInfoDto.builder()
                            .statusIntegration(handleInsertToPos)
                            .payload(payload)
                            .response(jsonNode.toPrettyString())
                            .build();

                }else{
                    log.info("Loi");
                    statusIntegration = "FAI";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            statusIntegration = "FAI";
        }


        return IntegrationHistoryInfoDto.builder()
                .statusIntegration(statusIntegration)
                .payload(payload)
                .build();
    }

    public List<KitchenOrderLineDto> getKitchenOrderLines(Integer orderId) {

        String sql = "SELECT d_kitchen_orderline_id, " +
                "qty," +
                "erp_product_id, description" +
                " FROM pos.d_int_kitchen_orderline_v WHERE d_tenant_id = :tenantId " +
                " AND d_kitchen_order_id = :orderId and is_active = 'Y' and orderline_status = 'PAI' ";

        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("orderId", orderId)
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        List<KitchenOrderLineDto> listOrderLine = new ArrayList<>();
        for ( Map<String, Object> row : results ) {
            log.info("Row: {}", row);

            KitchenOrderLineDto kitchenOrderLineDto = KitchenOrderLineDto.builder()
                    .m_Product_ID(ParseHelper.INT.parse(row.get("erp_product_id")))
                    .qty(ParseHelper.BIGDECIMAL.parse(row.get("qty")))
                    .note(ParseHelper.STRING.parse(row.get("description")))
                    .build();
            listOrderLine.add(kitchenOrderLineDto);
        }

        return listOrderLine;
    }

    public String convertDataFromERPtoPos(JsonNode jsonParentNode){

        log.info("Convert data from ERP to POS");
        log.info("jsonNode: " + jsonParentNode.toPrettyString());
        List<KitchenOrderDto> posOrderDtoList = new ArrayList<>();
        for (JsonNode node : jsonParentNode.path("data")) {

            if(node.path("success").asBoolean() == true) {
                log.info("Success");
                if(node.path("data") != null && !node.path("data").isEmpty()){
                    KitchenOrderDto kitchenOrderDto = KitchenOrderDto.builder()
                            .id(node.path("data").path("D_Kitchen_Order_ID").asInt())
                            .erpKitchenOrderId(node.path("data").path("Dbiz_Kitchen_Order_ID").asInt())
                            .isSyncErp("Y")
                            .build();
                    posOrderDtoList.add(kitchenOrderDto);
                }

            }

        }

        if(!posOrderDtoList.isEmpty()){
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<KitchenOrderDto>> requestEntity = new HttpEntity<>(posOrderDtoList, headers);

            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_API_INTEGRATION_KITCHEN_ORDER, requestEntity, GlobalReponse.class);

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
                if(tenantNumbers != dataSourceConfigService.getTenantNumbers()){
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
            }catch (Exception e) {
                e.printStackTrace();
            }

            if (value.getTenantId() != 0) {
                dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
            }else{
                dataSourceContextHolder.setCurrentTenantId(null);

            }
            AuditContext.setAuditInfo(new AuditInfo(0, 0,"0", "0",
                    0 ,"en",value.getTenantId()));
            ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");

            if(erpIntegration != null){
                log.info("Received message:");
                log.info("Key: " + key);
                log.info("Value: " + value);
                SyncIntegrationCredential credential = new SyncIntegrationCredential();
                credential.setOrgId(value.getOrgId());
                credential.setDataType("KDS");
                credential.setIntFlow("PTE");
//            credential.setIntType("INSERT");
//            credential.setFromType(1);
                syncIntegrationByKafka(credential,value);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers(){
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    public String syncIntegrationByKafka(SyncIntegrationCredential credential,
                                         SyncOrderInfoDto order) {
        log.info("sync integration kafka");
        String statusIntegration = "FAI";
        IntegrationHistoryInfoDto integrationHistoryInfoDto = null;


        try{

            integrationHistoryInfoDto = syncKitchenOrderIntegrationMainByOrder(credential,order.getKitchenOrderId());
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

        }catch (Exception e){
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
}
