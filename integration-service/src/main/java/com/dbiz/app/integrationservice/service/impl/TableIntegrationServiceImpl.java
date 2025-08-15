package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import com.dbiz.app.integrationservice.domain.KafkaSendEvent;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.repository.InterfaceIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.TableIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.TableKafkaDto;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.dto.orderDto.TableDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntTableCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TableIntegrationServiceImpl implements TableIntegrationService {
    private final MessageSource messageSource;
    private final ErpIntegrationRepository erpIntegrationRepository;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;
    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;
    @Autowired
    @Qualifier("externalRestTemplateProduct")
    private final RestTemplate externalRestTemplateProduct;
    private final CommonIntegrationService commonIntegrationService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final InterfaceIntegrationRepository interfaceIntegrationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final IntegrationHistoryRepository integrationHistoryRepository;

    @Override
    public IntegrationHistory tableIntegration(SyncIntegrationCredential credential) {
        log.info("**ServiceImpl::layer -  table integration **");
        StringBuilder error = new StringBuilder();
        GlobalReponse response = new GlobalReponse();
        IntegrationHistory integrationHistory = new IntegrationHistory();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }

        List<TableDto> getTableErp = getTableERP(credential, erpIntegration);

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<TableDto>> requestEntity = new HttpEntity<>(getTableErp, headers);


        if (credential.getIntType().equals("UPDATE") || (credential.getIntType().equals("INSERT") && getTableErp.size() < 30)) {
            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_TABLE_SAVE, requestEntity, GlobalReponse.class);
            if (response.getStatus() != HttpStatus.OK.value()) {
                error = new StringBuilder(response.getErrors());
            }
        }

        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        if (credential.getIntType().equals("INSERT") && credential.getSyncKafka().equals("Y"))
            integrationHistory.setIntStatus("INP");
        else
            integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(getTableErp));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;
    }

    @Transactional
    public List<TableDto> getTableERP(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp **");
        credential.setSyncKafka("N");
        List<TableDto> listTable = new ArrayList<>();
        boolean sendKafka = false;
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntTableCredential intTableCredential = IntTableCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .orgId(erpIntegration.getOrgId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.commonIntegrationService.getTokenIdempiere().getToken());


        try {
            HttpEntity<IntTableCredential> requestEntity = new HttpEntity<>(intTableCredential, headers);
            String url = erpIntegration.getErpUrl() + this.commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_TBL);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {
                        TableDto tableDto = TableDto.builder()
                                .isBuffet(node.path("isBuffet").asText())
                                // ad_user_id
                                .isActive(node.path("isActive").asText())
                                .numberSeats(node.path("seats").asInt())
                                .isRoom(node.path("isRoom").asText())
                                .floor(FloorDto.builder().erpFloorId(node.path("floorId").asInt()).build())
                                .orgId(node.path("AD_Org_ID").asInt())
                                .name(node.path("name").asText())
                                .numberGuests(node.path("numberGuests").decimalValue())
                                // bpartner_id
                                .erpTableId(node.path("tableId").asInt())
                                .isLocked(node.path("locked").asText())
                                .tableNo(node.path("tableNo").asText())
                                .build();
                        listTable.add(tableDto);

                        if (listTable.size() == 30 && credential.getIntType().equals("INSERT")) {
                            credential.setSyncKafka("Y");
                            try {
                                TableKafkaDto syncTable = TableKafkaDto.builder()
                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                                        .tableDtos(listTable)
                                        .lastPage("N")
                                        .build();
                                log.info("send message to kafka");
                                syncTable.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
                                List<JsonNode> jsonObjects = new ArrayList<>();

                                root.path("data").forEach(jsonObjects::add);
                                int batchSize = 30;
                                List<List<JsonNode>> batches = new ArrayList<>();
                                for (int j = 0; j < jsonObjects.size(); j += batchSize) {
                                    int end = Math.min(j + batchSize, jsonObjects.size());
                                    batches.add(jsonObjects.subList(j, end));
                                }
                                for (int j = 0; j < batches.size(); j++) {
                                    ArrayNode arrayNode = objectMapper.createArrayNode();
                                    arrayNode.addAll(batches.get(j));
                                    InterfaceIntegration intTBL = InterfaceIntegration.builder()
                                            .data(arrayNode)
                                            .type(AppConstant.DataTypeInt.TABLE)
                                            .status("NEW")
                                            .build();
                                    interfaceIntegrationRepository.saveAndFlush(intTBL);
                                }
                                sendKafka = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            listTable.clear();
                            sendKafka = true;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }
        if (credential.getIntType().equals("INSERT") && sendKafka) {
            try {
                TableKafkaDto syncTable = TableKafkaDto.builder()
                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                        .tableDtos(listTable)
                        .lastPage("Y")
                        .build();
                log.info("send message to kafka");
                syncTable.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
                eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_SEND_TBL, syncTable));
//                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_TBL, syncTable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listTable;
    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SEND_TBL, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageTBL(ConsumerRecord<String, TableKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        int intMess = 0;
        TableKafkaDto value = consumerRecord.value();
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
                "0", 0, "vi", value.getTenantId()));


        IntegrationHistory integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId(AppConstant.DataTypeInt.TABLE, AppConstant.IntStatus.INPROCESSING, AuditContext.getAuditInfo().getTenantId()).orElse(null);
        List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.TABLE, AppConstant.IntStatus.NEW);

        GlobalReponse response = new GlobalReponse();
        for (InterfaceIntegration i : interfaceInt) {
            List<TableDto> listTable = new ArrayList<>();
            for (JsonNode node : i.getData()) {
                TableDto tableDto = TableDto.builder()
                        .isBuffet(node.path("isBuffet").asText())
                        // ad_user_id
                        .isActive(node.path("isActive").asText())
                        .numberSeats(node.path("seats").asInt())
                        .isRoom(node.path("isRoom").asText())
                        .floor(FloorDto.builder().erpFloorId(node.path("floorId").asInt()).build())
                        .orgId(node.path("AD_Org_ID").asInt())
                        .name(node.path("name").asText())
                        .numberGuests(node.path("numberGuests").decimalValue())
                        // bpartner_id
                        .erpTableId(node.path("tableId").asInt())
                        .isLocked(node.path("locked").asText())
                        .tableNo(node.path("tableNo").asText())
                        .build();
                listTable.add(tableDto);
                response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_TABLE_SAVE, listTable, GlobalReponse.class);
                listTable.clear();
            }
            if (response.getStatus() == HttpStatus.OK.value()) {
                this.interfaceIntegrationRepository.delete(i);
            } else {
                i.setStatus(AppConstant.IntStatus.FAILURE);
                this.interfaceIntegrationRepository.save(i);
            }
            intMess++;
            if(intMess == 5)
            {
                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_TBL, value);
                break;
            }
        }
        boolean hasNewStatus = interfaceInt.stream()
                .anyMatch(item -> AppConstant.IntStatus.NEW.equals(item.getStatus()));

        if (!hasNewStatus || integrationHistory!=null) {
                integrationHistory.setIntStatus(AppConstant.IntStatus.COMPLETE);
                integrationHistory.setDescription(response.getMessage() + " " + response.getErrors());
                this.integrationHistoryRepository.save(integrationHistory);

        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }
}
