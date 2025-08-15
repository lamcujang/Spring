package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import com.dbiz.app.integrationservice.domain.KafkaSendEvent;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.repository.InterfaceIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.PriceListIntegrationService;
import com.dbiz.app.integrationservice.service.ProductIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
import org.common.dbiz.dto.integrationDto.IntegrationProductDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntOrgCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPriceListCredential;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceListIntegrationServiceImpl implements PriceListIntegrationService {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);


    private final IntegrationHistoryRepository integrationHistoryRepository;

    private final ModelMapper modelMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Autowired
    @Qualifier("externalRestTemplateProduct")
    private final RestTemplate externalRestTemplateProduct;

    private final CommonIntegrationService commonService;

    private final ObjectMapper objectMapper;

    private StringBuilder payloadGloabl = new StringBuilder();

    private StringBuilder responseGlobal = new StringBuilder();


    private final DataSourceContextHolder dataSourceContextHolder;
    private final DataSourceConfigService dataSourceConfigService;
    private final DataSourceRoutingService dataSourceRoutingService;
    private final String GROUP_ID = "gr-sync-order";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final InterfaceIntegrationRepository interfaceIntegrationRepository;

    private String result = "Y";

    private final ApplicationEventPublisher eventPublisher;

    /**
     * @param credential
     * @return
     */
    @Override
    @Transactional
    public IntegrationHistoryInfoDto syncPriceListErp(SyncIntegrationCredential credential) {
        String result = "COM";
        log.info("*** Start get product from erp ***");
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        IntPriceListCredential intPriceListCredential = IntPriceListCredential.builder()
                .clientId(tokenIdempiereRespDto.getAdClientId())
                .orgId(tokenIdempiereRespDto.getOrgId())
                .intType(credential.getIntType())
                .intDate(castIntDate(credential)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        try {
            payloadGloabl.append(objectMapper.writeValueAsString(intPriceListCredential));
            HttpEntity<IntPriceListCredential> requestEntity = new HttpEntity<>(intPriceListCredential, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRL);
            log.info("url integration org: " + url);

            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {

                try {
                    String responseBody = response.getBody();
                    JsonNode root = objectMapper.readTree(responseBody);
                    List<PriceListIntDto> listPriceList = new ArrayList<>();
                    responseGlobal.append(root.toString());
                    List<JsonNode> dataNodes = new ArrayList<>();
                    root.path("data").forEach(dataNodes::add);

                    for (int i = 0; i < dataNodes.size(); i++) {
                        JsonNode node = dataNodes.get(i);
                        PriceListIntDto priceInt = PriceListIntDto.builder()
                                .pricePrecision(node.get("pricePrecision").asInt())
                                .isDefault(node.get("isDefault").asText())
                                .isSaleprice(node.get("isSOPriceList").asText())
                                .orgId(node.get("AD_Org_ID").asInt())
                                .name(node.get("name").asText())
                                .erpPriceListId(node.get("M_PriceList_ID").asInt())
                                .fromDate(node.get("validFrom").asText())
                                .isActive(node.get("isActive").asText())
                                .maxDate(node.get("maxDate").asText())
                                .build();

                        String priceListProductObject = node.has("priceDetails") ? node.get("priceDetails").asText() : null;
                        if (priceListProductObject != null) {
                            JSONArray priceListProductObjectJson = new JSONArray(priceListProductObject);
                            List<PriceListProductDto> listPriceProduct = new ArrayList<>();
                            for (int a = 0; a < priceListProductObjectJson.length(); a++) {
                                JSONObject item = priceListProductObjectJson.getJSONObject(a);
                                PriceListProductDto priceListProductDto = PriceListProductDto.builder()
                                        .productId(item.getInt("m_product_id"))
                                        .standardPrice(item.getBigDecimal("pricestd"))
                                        .salesPrice(item.getBigDecimal("pricestd"))
                                        .lastOrderPrice(BigDecimal.ZERO)
                                        .costPrice(BigDecimal.ZERO)

                                        .build();
                                priceListProductDto.setIsActive(item.getString("isactive"));
                                listPriceProduct.add(priceListProductDto);
                            }
                            priceInt.setListPriceProduct(listPriceProduct);
                        }
                        listPriceList.add(priceInt);
                        // neu la insert va co nhieu hon 3 data node thi gui len kafka
                        if (credential.getIntType().equals("INSERT") && dataNodes.size() > 3) {
//                            if (i == dataNodes.size() - 1) {
//                                try {
//                                    log.info("send message to kafka");
//                                    PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
//                                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                            .priceListIntDtoList(listPriceList)
//                                            .syncIntegrationCredential(credential)
//                                            .lastPage("Y")
//                                            .build();
////                                    kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                            try {
//                                log.info("send message to kafka");
//                                PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
//                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                        .priceListIntDtoList(listPriceList)
//                                        .syncIntegrationCredential(credential)
//                                        .lastPage("N")
//                                        .build();
////                                kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            listPriceList.clear();
//                            }
                            PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
                                    .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                                    .priceListIntDtoList(listPriceList)
                                    .syncIntegrationCredential(credential)
                                    .lastPage("Y")
                                    .build();
                            InterfaceIntegration intF = InterfaceIntegration.builder()
                                    .data(root.path("data"))
                                    .type(AppConstant.DataTypeInt.PRICE_LIST)
                                    .status(AppConstant.IntStatus.NEW)
                                    .build();
                            this.interfaceIntegrationRepository.save(intF);
                              eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_PRICELIST, priceListIntKafkaDto));
                            credential.setSyncKafka("Y");
                            break;
                        }else if(credential.getIntType().equals("UPDATE") && dataNodes.size() > 2) {
                            PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
                                    .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                                    .priceListIntDtoList(listPriceList)
                                    .syncIntegrationCredential(credential)
                                    .lastPage("Y")
                                    .build();
                            InterfaceIntegration intF = InterfaceIntegration.builder()
                                    .data(root.path("data"))
                                    .type(AppConstant.DataTypeInt.PRICE_LIST)
                                    .status(AppConstant.IntStatus.NEW)
                                    .build();
                            this.interfaceIntegrationRepository.save(intF);
                            eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_PRICELIST, priceListIntKafkaDto));
                            credential.setSyncKafka("Y");
                            break;
                        }

                        else
                            credential.setSyncKafka("N");
                    }

                    result = syncPrice(listPriceList, credential);
                } catch (Exception e) {
                    result = "FAI";
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            result = "FAI";
            e.printStackTrace();
        }


        return IntegrationHistoryInfoDto.builder()
                .payload(payloadGloabl.toString())
                .response(responseGlobal.toString())
                .statusIntegration(result)
                .build();
    }

    private String syncPrice(List<PriceListIntDto> param, SyncIntegrationCredential credential) {
        GlobalReponse response = new GlobalReponse();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<PriceListIntDto>> requestEntity = new HttpEntity<>(param, headers);

        if (param.size() <= 3 && credential.getSyncKafka().equals("N")) {
            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRICELIST, requestEntity, GlobalReponse.class);

            if (response.getStatus().equals(HttpStatus.OK.value()))
                return "COM";
            else
                return "FAI";
        }
//            try {
//                log.info("send message to kafka");
//                PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
//                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                        .priceListIntDtoList(param)
//                        .syncIntegrationCredential(credential)
//                        .build();
//                kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        return "INP";
    }


    @KafkaListener(groupId = GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_PRICELIST, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessage(ConsumerRecord<String, PriceListIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        PriceListIntKafkaDto value = consumerRecord.value();
        String Status = "FAI";
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

        List<InterfaceIntegration> list = this.interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.PRICE_LIST,AppConstant.IntStatus.NEW);
        for(InterfaceIntegration item :list)
        {
            List<JsonNode> dataNodes = new ArrayList<>();
            item.getData().forEach(dataNodes::add);
            List<PriceListIntDto> listPriceList = new ArrayList<>();
            for (int i = 0; i < dataNodes.size(); i++) {
                JsonNode node = dataNodes.get(i);
                PriceListIntDto priceInt = PriceListIntDto.builder()
                        .pricePrecision(node.get("pricePrecision").asInt())
                        .isDefault(node.get("isDefault").asText())
                        .isSaleprice(node.get("isSOPriceList").asText())
                        .orgId(node.get("AD_Org_ID").asInt())
                        .name(node.get("name").asText())
                        .erpPriceListId(node.get("M_PriceList_ID").asInt())
                        .fromDate(node.get("validFrom").asText())
                        .isActive(node.get("isActive").asText())
                        .maxDate(node.get("maxDate").asText())
                        .build();

                String priceListProductObject = node.has("priceDetails") ? node.get("priceDetails").asText() : null;
                if (priceListProductObject != null) {
                    JSONArray priceListProductObjectJson = new JSONArray(priceListProductObject);
                    List<PriceListProductDto> listPriceProduct = new ArrayList<>();
                    for (int a = 0; a < priceListProductObjectJson.length(); a++) {
                        JSONObject itemP = priceListProductObjectJson.getJSONObject(a);
                        PriceListProductDto priceListProductDto = PriceListProductDto.builder()
                                .productId(itemP.getInt("m_product_id"))
                                .standardPrice(itemP.getBigDecimal("pricestd"))
                                .salesPrice(itemP.getBigDecimal("pricestd"))
                                .lastOrderPrice(BigDecimal.ZERO)
                                .costPrice(BigDecimal.ZERO)
                                .build();
                        priceListProductDto.setIsActive(itemP.getString("isactive"));
                        listPriceProduct.add(priceListProductDto);
                    }
                    priceInt.setListPriceProduct(listPriceProduct);
                }
                listPriceList.add(priceInt);
            }
            Status = this.syncPriceKaka(listPriceList,value.getSyncIntegrationCredential());
            this.interfaceIntegrationRepository.delete(item);
            this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_PRICELIST, value);
            break;
        }

        try {
            acknowledgment.acknowledge();
            log.info("Message processed successfully");
        } catch (Exception e) {
            log.error("Error processing message, will retry", e);
        }
        finally {
            if(list.isEmpty())
            {

                IntegrationHistory integrationHistory = integrationHistoryRepository.findById(value.getSyncIntegrationCredential().getIntegrationHistoryId()).orElse(null);
                if (integrationHistory != null ) {
                    integrationHistory.setIntStatus("COM");
                    integrationHistoryRepository.save(integrationHistory);
                }
            }
        }

    }
    private String syncPriceKaka(List<PriceListIntDto> param, SyncIntegrationCredential credential) {
            this.commonService.sendPostRequest(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRICELIST, param,GlobalReponse.class );
            return "COM";
    }
    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private String castIntDate(SyncIntegrationCredential credential) {
        StringBuilder formattedDate;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (credential.getFromType() == 2) {
            LocalDateTime dateTime = LocalDateTime.parse(credential.getDate() == null ? credential.getIntDate() : credential.getDate(), inputFormatter);
            formattedDate = new StringBuilder(dateTime.format(outputFormatter));
            return formattedDate.toString();
        } else {
            IntegrationHistory integrationHistory = integrationHistoryRepository.findLatestIntegrationDate(credential.getDataType());
            if (integrationHistory != null) {
                LocalDateTime localDateTime = LocalDateTime.ofInstant(integrationHistory.getIntDate(), ZoneId.systemDefault());
                formattedDate = new StringBuilder(localDateTime.format(outputFormatter));
                return formattedDate.toString();
            } else {
                formattedDate = new StringBuilder(LocalDateTime.now().format(outputFormatter));
                return formattedDate.toString();
            }
        }
    }
}




//package com.dbiz.app.integrationservice.service.impl;
//
//import com.dbiz.app.integrationservice.constant.AppConstant;
//import com.dbiz.app.integrationservice.domain.IntegrationHistory;
//import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
//import com.dbiz.app.integrationservice.service.CommonIntegrationService;
//import com.dbiz.app.integrationservice.service.PriceListIntegrationService;
//import com.dbiz.app.integrationservice.service.ProductIntegrationService;
//import com.dbiz.app.tenantservice.domain.AuditContext;
//import com.dbiz.app.tenantservice.domain.AuditInfo;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
//import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.common.dbiz.dto.integrationDto.IntegrationHistoryInfoDto;
//import org.common.dbiz.dto.integrationDto.IntegrationProductDto;
//import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
//import org.common.dbiz.dto.productDto.*;
//import org.common.dbiz.payload.GlobalReponse;
//import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
//import org.common.dbiz.request.intergrationRequest.external.IntOrgCredential;
//import org.common.dbiz.request.intergrationRequest.external.IntPriceListCredential;
//import org.json.HTTP;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.http.*;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class PriceListIntegrationServiceImpl implements PriceListIntegrationService {
//
//
//    private final IntegrationHistoryRepository integrationHistoryRepository;
//
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    @Qualifier("restTemplateBean")
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    @Qualifier("externalRestTemplate")
//    private final RestTemplate externalRestTemplate;
//
//    @Autowired
//    @Qualifier("externalRestTemplateProduct")
//    private final RestTemplate externalRestTemplateProduct;
//
//    private final CommonIntegrationService commonService;
//
//    private final ObjectMapper objectMapper;
//
//    private StringBuilder payloadGloabl = new StringBuilder();
//
//    private StringBuilder responseGlobal = new StringBuilder();
//
//
//    private final DataSourceContextHolder dataSourceContextHolder;
//    private final DataSourceConfigService dataSourceConfigService;
//    private final DataSourceRoutingService dataSourceRoutingService;
//    private final String GROUP_ID = "gr-sync-order";
//    private final static String TOPIC = "sync-integration-to-pricelist";
//
//    private final static String TOPIC2 = "sync-pricelist-to-integration";
//
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    private String result = "Y";
//
//    /**
//     * @param credential
//     * @return
//     */
//    @Override
//    public IntegrationHistoryInfoDto syncPriceListErp(SyncIntegrationCredential credential) {
//        String result = "COM";
//        log.info("*** Start get product from erp ***");
//        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
//
//        IntPriceListCredential intPriceListCredential = IntPriceListCredential.builder()
//                .clientId(tokenIdempiereRespDto.getAdClientId())
//                .orgId(tokenIdempiereRespDto.getOrgId())
//                .intType(credential.getIntType())
//                .intDate(castIntDate(credential)).build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
//        try {
//            payloadGloabl.append(objectMapper.writeValueAsString(intPriceListCredential));
//            HttpEntity<IntPriceListCredential> requestEntity = new HttpEntity<>(intPriceListCredential, headers);
//            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRL);
//            log.info("url integration org: " + url);
//
//            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//            if (response.getStatusCode().is2xxSuccessful()) {
//
//                try {
//                    String responseBody = response.getBody();
//                    JsonNode root = objectMapper.readTree(responseBody);
//                    List<PriceListIntDto> listPriceList = new ArrayList<>();
//                    responseGlobal.append(root.toString());
//                    List<JsonNode> dataNodes = new ArrayList<>();
//                    root.path("data").forEach(dataNodes::add);
//
//                    for (int i = 0; i < dataNodes.size(); i++) {
//                        JsonNode node = dataNodes.get(i);
//                        PriceListIntDto priceInt = PriceListIntDto.builder()
//                                .pricePrecision(node.get("pricePrecision").asInt())
//                                .isDefault(node.get("isDefault").asText())
//                                .isSaleprice(node.get("isSOPriceList").asText())
//                                .orgId(node.get("AD_Org_ID").asInt())
//                                .name(node.get("name").asText())
//                                .erpPriceListId(node.get("M_PriceList_ID").asInt())
//                                .fromDate(node.get("validFrom").asText())
//                                .isActive(node.get("isActive").asText())
//                                .maxDate(node.get("maxDate").asText())
//                                .build();
//
//                        String priceListProductObject = node.has("priceDetails") ? node.get("priceDetails").asText() : null;
//                        if (priceListProductObject != null) {
//                            JSONArray priceListProductObjectJson = new JSONArray(priceListProductObject);
//                            List<PriceListProductDto> listPriceProduct = new ArrayList<>();
//                            for (int a = 0; a < priceListProductObjectJson.length(); a++) {
//                                JSONObject item = priceListProductObjectJson.getJSONObject(a);
//                                PriceListProductDto priceListProductDto = PriceListProductDto.builder()
//                                        .productId(item.getInt("m_product_id"))
//                                        .standardPrice(item.getBigDecimal("pricestd"))
//                                        .salesPrice(item.getBigDecimal("pricestd"))
//                                        .lastOrderPrice(BigDecimal.ZERO)
//                                        .costPrice(BigDecimal.ZERO)
//
//                                        .build();
//                                listPriceProduct.add(priceListProductDto);
//                            }
//                            priceInt.setListPriceProduct(listPriceProduct);
//                        }
//                        listPriceList.add(priceInt);
//                        // neu la insert va co nhieu hon 3 data node thi gui len kafka
//                        if (credential.getIntType().equals("INSERT") && dataNodes.size() > 3) {
//                            if (i == dataNodes.size() - 1) {
//                                try {
//                                    log.info("send message to kafka");
//                                    PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
//                                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                            .priceListIntDtoList(listPriceList)
//                                            .syncIntegrationCredential(credential)
//                                            .lastPage("Y")
//                                            .build();
//                                    kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                try {
//                                    log.info("send message to kafka");
//                                    PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
//                                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                            .priceListIntDtoList(listPriceList)
//                                            .syncIntegrationCredential(credential)
//                                            .lastPage("N")
//                                            .build();
//                                    kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                listPriceList.clear();
//                            }
//                            credential.setSyncKafka("Y");
//                        }
//                        else
//                            credential.setSyncKafka("N");
//                    }
//
//                    result = syncPrice(listPriceList, credential);
//                } catch (Exception e) {
//                    result = "FAI";
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            result = "FAI";
//            e.printStackTrace();
//        }
//
//
//        return IntegrationHistoryInfoDto.builder()
//                .payload(payloadGloabl.toString())
//                .response(responseGlobal.toString())
//                .statusIntegration(result)
//                .build();
//    }
//
//    private String syncPrice(List<PriceListIntDto> param, SyncIntegrationCredential credential) {
//        GlobalReponse response = new GlobalReponse();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
//        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
//        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
//        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
//        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
//        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<PriceListIntDto>> requestEntity = new HttpEntity<>(param, headers);
//
//        if (param.size() <= 3  && credential.getSyncKafka().equals("N") )
//        {
//            response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRICELIST, requestEntity, GlobalReponse.class);
//
//            if(response.getStatus().equals(HttpStatus.OK.value()))
//                return "COM";
//            else
//                return "FAI";
//        }
////            try {
////                log.info("send message to kafka");
////                PriceListIntKafkaDto priceListIntKafkaDto = PriceListIntKafkaDto.builder()
////                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
////                        .priceListIntDtoList(param)
////                        .syncIntegrationCredential(credential)
////                        .build();
////                kafkaTemplate.send(TOPIC, priceListIntKafkaDto);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
//        return "INP";
//    }
//
//
//    @KafkaListener(groupId = GROUP_ID, topics = TOPIC2, containerFactory = "kafkaListenerContainerFactory")
//    public void receivedMessage(ConsumerRecord<String, PriceListIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
//        PriceListIntKafkaDto value = consumerRecord.value();
//        try {
//            int tenantNumbers = getTenantNumbers();
//            if (tenantNumbers != dataSourceConfigService.getTenantNumbers()) {
//                Map<Object, Object> configuredDataSources = dataSourceConfigService
//                        .configureDataSources();
//
//                dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
//            }
////                if(dataSourceConfigService.checkExistTenantRedis(value.getTenantId()) == 0){
////                    Map<Object, Object> configuredDataSources = dataSourceConfigService
////                            .configureDataSources();
////
////                    dataSourceRoutingService.updateResolvedDataSources(configuredDataSources);
////                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (value.getTenantId() != 0) {
//            dataSourceContextHolder.setCurrentTenantId(new Long(value.getTenantId()));
//        } else {
//            dataSourceContextHolder.setCurrentTenantId(null);
//
//        }
//        AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
//                "0", 0, "vi", value.getTenantId()));
//
//
//        PriceListIntKafkaDto integrationProductDto = value;
////        IntegrationHistory integrationHistory = IntegrationHistory.builder()
////                .orgId(integrationProductDto.getSyncIntegrationCredential().getOrgId())
////                .intType(integrationProductDto.getSyncIntegrationCredential().getDataType())
////                .intFlow(integrationProductDto.getSyncIntegrationCredential().getIntFlow())
////                .intStatus(integrationProductDto.getStatusIntegration())
////                .intDate(Instant.now())
////                .userId(integrationProductDto.getSyncIntegrationCredential().getUserId())
////                .tenantId(AuditContext.getAuditInfo().getTenantId())
////                .payload(integrationProductDto.getPayload())
////                .response(integrationProductDto.getResponse())
////                .orgId(0)
////                .description(integrationProductDto.getError())
////                .build();
////        this.integrationHistoryRepository.save(integrationHistory);
//        IntegrationHistory integrationHistory = integrationHistoryRepository.findById(value.getSyncIntegrationCredential().getIntegrationHistoryId()).orElse(null);
//        if (integrationHistory != null && value.getLastPage().equals("Y")) {
//            integrationHistory.setIntStatus(value.getStatusIntegration());
//            integrationHistory.setResponse(value.getResponse());
//            integrationHistory.setPayload(value.getPayload());
//            integrationHistoryRepository.save(integrationHistory);
//        }
//        acknowledgment.acknowledge();
//    }
//
//    public int getTenantNumbers() {
//        return dataSourceConfigService.getTenantNumbersRedis();
//    }
//
//    private String castIntDate(SyncIntegrationCredential credential) {
//        StringBuilder formattedDate;
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        if (credential.getFromType() == 2) {
//            LocalDateTime dateTime = LocalDateTime.parse(credential.getDate() == null ? credential.getIntDate() : credential.getDate(), inputFormatter);
//            formattedDate = new StringBuilder(dateTime.format(outputFormatter));
//            return formattedDate.toString();
//        } else {
//            IntegrationHistory integrationHistory = integrationHistoryRepository.findLatestIntegrationDate(credential.getDataType());
//            if (integrationHistory != null) {
//                LocalDateTime localDateTime = LocalDateTime.ofInstant(integrationHistory.getIntDate(), ZoneId.systemDefault());
//                formattedDate = new StringBuilder(localDateTime.format(outputFormatter));
//                return formattedDate.toString();
//            } else {
//                formattedDate = new StringBuilder(LocalDateTime.now().format(outputFormatter));
//                return formattedDate.toString();
//            }
//        }
//    }
//}
