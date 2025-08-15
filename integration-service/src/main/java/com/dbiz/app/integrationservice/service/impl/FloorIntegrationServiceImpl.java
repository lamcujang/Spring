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
import com.dbiz.app.integrationservice.service.FloorIntegrationService;
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
import org.common.dbiz.dto.integrationDto.FloorKafkaDto;
import org.common.dbiz.dto.orderDto.FloorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntFloorCredential;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FloorIntegrationServiceImpl implements FloorIntegrationService {
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
    @Transactional
    public IntegrationHistory floorIntegration(SyncIntegrationCredential credential) {
        StringBuilder error = new StringBuilder();
        GlobalReponse response = new GlobalReponse();
        log.info("**ServiceImpl::layer -  floor integration **");

        IntegrationHistory integrationHistory = new IntegrationHistory();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }

        List<FloorDto> getFloorErp = getFloorErp(credential, erpIntegration);

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<FloorDto>> requestEntity = new HttpEntity<>(getFloorErp, headers);

        if (credential.getSyncKafka().equals("N")) {
            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_FLOOR_SAVE, requestEntity, GlobalReponse.class);

            if (response.getStatus() != HttpStatus.OK.value())
                error = new StringBuilder(response.getErrors());

        }
        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        if (credential.getSyncKafka().equals("N"))
            integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        else
            integrationHistory.setIntStatus("INP");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(getFloorErp));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;
    }

    @Transactional
    public List<FloorDto> getFloorErp(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        credential.setSyncKafka("N");
        log.info("** getFloorErp **");
        List<FloorDto> listFloor = new ArrayList<>();
        boolean sendKafka = false;

        IntFloorCredential intFloorCredential = IntFloorCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .orgId(erpIntegration.getOrgId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.commonIntegrationService.getTokenIdempiere().getToken());

        try {
            HttpEntity<IntFloorCredential> requestEntity = new HttpEntity<>(intFloorCredential, headers);
            String url = erpIntegration.getErpUrl() + this.commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_FLO);
            log.info("url integration floor: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    // khong lay c_bpartner_id
                    for (JsonNode node : root.path("data")) {
                        FloorDto floorDto = FloorDto.builder()
                                .floorNo(node.path("floorNo").asText())
                                .isActive(node.path("isActive").asText())
                                .name(node.path("name").asText())
                                .posTerminalId(node.path("C_POS_ID").asInt())
                                .orgId(node.path("AD_Org_ID").asInt())
                                .erpFloorId(node.path("floorId").asInt())
                                .build();
                        listFloor.add(floorDto);
                        if (listFloor.size() == 30 && credential.getIntType().equals("INSERT")) {
                            try {
//                                log.info("send message to kafka");
//                                FloorKafkaDto floorKafkaDto = FloorKafkaDto.builder()
//                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                        .floorDtos(listFloor)
//                                        .lastPage("N")
//                                        .build();
//                                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_FLO, floorKafkaDto);
                                List<JsonNode> jsonObjects = new ArrayList<>();
                                root.path("data").forEach(jsonObjects::add);
                                int batchSize = 50;
                                List<List<JsonNode>> batches = new ArrayList<>();
                                for (int j = 0; j < jsonObjects.size(); j += batchSize) {
                                    int end = Math.min(j + batchSize, jsonObjects.size());
                                    batches.add(jsonObjects.subList(j, end));
                                }
                                for (int j = 0; j < batches.size(); j++) {
                                    ArrayNode arrayNode = objectMapper.createArrayNode();
                                    arrayNode.addAll(batches.get(j));
                                    InterfaceIntegration intFloor = InterfaceIntegration.builder()
                                            .data(arrayNode)
                                            .type(AppConstant.DataTypeInt.FLOOR)
                                            .status("NEW")
                                            .build();
                                    interfaceIntegrationRepository.save(intFloor);
                                }
                                sendKafka = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            credential.setSyncKafka("Y");
                            listFloor.clear();
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
        if (sendKafka) {
            credential.setSyncKafka("Y");
            log.info("send message to kafka");
            FloorKafkaDto floorKafkaDto = FloorKafkaDto.builder()
                    .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                    .floorDtos(listFloor)
                    .lastPage("Y")
                    .build();
            eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_SEND_FLO, floorKafkaDto));
        }
        return listFloor;
    }


    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SEND_FLO, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessage(ConsumerRecord<String, FloorKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received message: " + consumerRecord.value());
        try {
            String key = consumerRecord.key(); // could be null
            FloorKafkaDto value = consumerRecord.value();

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
            log.info("Received message:");
            log.info("Key: " + key);
            List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.FLOOR, "NEW");
            List<FloorDto> listFloor = new ArrayList<>();
            GlobalReponse response = new GlobalReponse();
            for (InterfaceIntegration i : interfaceInt) {
                for (JsonNode node : i.getData()) {
                    FloorDto floorDto = FloorDto.builder()
                            .floorNo(node.path("floorNo").asText())
                            .isActive(node.path("isActive").asText())
                            .name(node.path("name").asText())
                            .posTerminalId(node.path("C_POS_ID").asInt())
                            .orgId(node.path("AD_Org_ID").asInt())
                            .erpFloorId(node.path("floorId").asInt())
                            .build();
                    listFloor.add(floorDto);
                    if (listFloor.size() >= 30) {
                        response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_FLOOR_SAVE, listFloor, GlobalReponse.class);
                        listFloor.clear();
                    }
                }
                if (!listFloor.isEmpty()) {
                    response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.ORDER_SERVICE_INT_FLOOR_SAVE, listFloor, GlobalReponse.class);
                }
                if (response.getStatus().equals(HttpStatus.OK))
                    this.interfaceIntegrationRepository.delete(i);
                else {
                    i.setStatus(AppConstant.IntStatus.FAILURE);
                    this.interfaceIntegrationRepository.save(i);
                }
            }
            IntegrationHistory integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantId(AppConstant.DataTypeInt.FLOOR, AppConstant.IntStatus.INPROCESSING, AuditContext.getAuditInfo().getTenantId())
                    .orElse(null);
            if (integrationHistory != null) {
                integrationHistory.setIntStatus(AppConstant.IntStatus.COMPLETE);
                integrationHistory.setDescription(response.getMessage() + " " + response.getErrors());
                this.integrationHistoryRepository.save(integrationHistory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        acknowledgment.acknowledge();
    }
    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

}
