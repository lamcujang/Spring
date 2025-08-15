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
import com.dbiz.app.integrationservice.service.ProductIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.PcTerminalAccessDto;
import org.common.dbiz.dto.integrationDto.*;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.productDto.*;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.IntPartnerDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntProductCategoryCredential;
import org.common.dbiz.request.intergrationRequest.external.IntUserCredential;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// chuan bij change huong moi day la flag de ctrlz
// 17 - 02 - 20210 doi co che dong bo thay vi  day data vao message kafka day thi day data vao bang interface
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductIntegrationServiceImpl implements ProductIntegrationService {

    private final MessageSource messageSource;
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

    private StringBuilder payload = new StringBuilder();

    private StringBuilder responseGlobal = new StringBuilder();

    private final DataSourceContextHolder dataSourceContextHolder;

    private final DataSourceConfigService dataSourceConfigService;

    private final DataSourceRoutingService dataSourceRoutingService;

    private String resultSync = "COM";

    private final KafkaTemplate<String, Object> kafkaTemplate;

//    private final static String TOPIC2 = "sync-product-to-integration";

    private final String GROUP_ID = "gr-sync-order";

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final InterfaceIntegrationRepository interfaceIntegrationRepository;

    private final ApplicationEventPublisher eventPublisher;


    /**
     * dong bo du lieu san pham tu erp
     * dong bo product , uom, tax category , product category , image ( save image -> mdm )
     *
     * @param credential
     * @param
     * @return
     */
    @Override
    @Transactional
    public IntegrationHistoryInfoDto getProductErp(SyncIntegrationCredential credential) {
        StringBuilder error = new StringBuilder();
        log.info("*** Start get product from erp ***");
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE)) {
            TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenIdempiereRespDto.getToken());
            try {
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
                url.append(commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRO)).append("?");
                url.append("$top=5");
                url.append("&$filter=AD_Client_ID eq ").append(tokenIdempiereRespDto.getAdClientId());//.append(" and contains(image_product,'/') and IsStocked eq true and IsBundle eq false ");// .append(" AND contains(Name,'Thịt')");
                if (credential.getIntType().equals("UPDATE"))
                    url.append(" AND Updated_Date ge '").append(commonService.castDateYYYYmmDD(credential)).append("'");
                url.append("&$skip=" + credential.getLineProduct());
                url.append("&$select=M_Product_ID,value,name,description,isstocked,issold,ispurchased, isbundle, iscom, isbuffet, isbom, producttype,tax_category_object, UOM, product_category_object,assign_org_object, product_location_object, bom_object");//image_product

                payload.append(url.toString());
                ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String responseBody = response.getBody();
                    responseGlobal.append(responseBody);
                    try {
                        JsonNode root = objectMapper.readTree(responseBody);
                        Integer pageCount = root.get("page-count").asInt();
                        Integer recordsSize = root.get("records-size").asInt();
                        Integer skipRecords = root.get("skip-records").asInt();
                        Integer rowCount = root.get("row-count").asInt();
                        JsonNode records = root.get("records");
//                        if (rowCount > 10 && credential.getSyncKafka().equals("N") && credential.getIntType().equals("INSERT")) {
                        if (rowCount > 5) {
                            if ((skipRecords >= rowCount)) {
                                IntegrationHistory integrationHistory = integrationHistoryRepository.findById(credential.getIntegrationHistoryId()).orElse(null);
                                if (integrationHistory != null) {
                                    integrationHistory.setIntStatus("COM");
                                    this.integrationHistoryRepository.saveAndFlush(integrationHistory);
                                }
                                this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_SYNC_IMAGE_PRODUCT, credential);
                                return IntegrationHistoryInfoDto.builder()
                                        .payload("")
                                        .response("")
                                        .statusIntegration("COM")
                                        .error("")
                                        .build();
                            }
                            InterfaceIntegration intSave = InterfaceIntegration.builder()
                                    .data(records)
                                    .type(AppConstant.DataTypeInt.PRODUCT)
                                    .status("NEW")
                                    .build();
                            this.interfaceIntegrationRepository.saveAndFlush(intSave);

                            try {
                                credential.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
                                credential.setSyncKafka("Y");
                                credential.setLineProduct(credential.getLineProduct() + 5);
                                SyncIntegrationCredential creNew = SyncIntegrationCredential.builder()
                                        .orgId(credential.getOrgId() != null ? credential.getOrgId() : 0)
                                        .intFlow(credential.getIntFlow() != null ? credential.getIntFlow() : "")
                                        .dataType(credential.getDataType() != null ? credential.getDataType() : "")
                                        .intType(credential.getIntType() != null ? credential.getIntType() : "")
                                        .fromType(credential.getFromType() != null ? credential.getFromType() : 0)
                                        .date(credential.getDate() != null ? credential.getDate() : "")
                                        .intDate(credential.getIntDate() != null ? credential.getIntDate() : "")
                                        .userId(credential.getUserId() != null ? credential.getUserId() : 0)
                                        .integrationHistoryId(credential.getIntegrationHistoryId() != null ? credential.getIntegrationHistoryId() : 0)
                                        .tenantId(credential.getTenantId() != null ? credential.getTenantId() : 0)
                                        .status(credential.getStatus() != null ? credential.getStatus() : "")
                                        .error(credential.getError() != null ? credential.getError() : "")
                                        .syncKafka(credential.getSyncKafka() != null ? credential.getSyncKafka() : "N")
                                        .outputFormatter(credential.getOutputFormatter() != null ? credential.getOutputFormatter() : "dd-MM-yyyy")
                                        .lineProduct(credential.getLineProduct() != null ? credential.getLineProduct() : 0)
                                        .build();
                                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_INTERNAL_PRODUCT, creNew);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return IntegrationHistoryInfoDto.builder()
                                    .payload("")
                                    .response("")
                                    .statusIntegration("INP")
                                    .error("")
                                    .build();
                        } else {
                            GlobalReponse status = this.syncProduct(this.castProductInt(records), credential);

                            return IntegrationHistoryInfoDto.builder()
                                    .payload(objectMapper.writeValueAsString(records))
                                    .response("")
                                    .statusIntegration(status.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI")
                                    .error(status.getErrors())
                                    .build();
                        }
                    } catch (Exception e) {
                        error.append(e.getMessage());
                        resultSync = "FAI";
                        responseGlobal.append(e.getMessage());
                        e.printStackTrace();
                        log.error("Error when get data from erp: " + e.getMessage());
                    }

                }

                // end if
            } catch (Exception e) {
                error.append(e.getMessage());
                resultSync = "FAI";
                e.printStackTrace();
                responseGlobal.append(e.getMessage());
                log.error("Error when get data from erp: " + e.getMessage());
            }

        } else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT)) {

        }


        return IntegrationHistoryInfoDto.builder()
                .payload(payload.toString())
                .response(responseGlobal.toString())
                .statusIntegration(resultSync)
                .error(error.toString())
                .build();
    }

    /**
     * @param credential
     * @return
     */
    @Override
    public IntegrationHistory productCategoryIntegration(SyncIntegrationCredential credential) {
        StringBuilder error = new StringBuilder();
        log.info("**ServiceImpl::layer -  product category integration **");
        IntegrationHistory integrationHistory = new IntegrationHistory();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        GlobalReponse response = new GlobalReponse();
        List<ProductCategoryDto> getProductCategory = new ArrayList<>();
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getProductCategory = getProductCategoryIdempiere(credential, erpIntegration);
            ProductCatIntDto productCatIntDto = ProductCatIntDto.builder()
                    .productCategoryDtos(getProductCategory)
                    .type(AppConstant.ERP_PLATFORM_IDEMPIERE).build();

            response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRODUCT_CATEGORY, productCatIntDto, GlobalReponse.class);
            if (response.getStatus() != HttpStatus.OK.value()) {
                error = new StringBuilder(response.getErrors());
            }
        } else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getProductCategory = getProductCategoryERPNext(credential, erpIntegration);
            ProductCatIntDto productCatIntDto = ProductCatIntDto.builder()
                    .productCategoryDtos(getProductCategory)
                    .type(AppConstant.ERP_PLATFORM_ERPNEXT).build();
            response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRODUCT_CATEGORY, productCatIntDto, GlobalReponse.class);
            if (response.getStatus() != HttpStatus.OK.value()) {
                error = new StringBuilder(response.getErrors());
            }
        }


        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(getProductCategory));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;
    }

    /**
     *
     * @param credential
     * @return
     */
    @Override
    public GlobalReponse productIntegration(SyncIntegrationCredential credential) {
        this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_SYNC_IMAGE_PRODUCT, credential);
        return GlobalReponse.builder().status(HttpStatus.OK.value()).build();
    }

    @Transactional
    public Integer getProductIdempiere(Integer rowCount, SyncIntegrationCredential credential) {
        for (int j = 5; j <= rowCount; j += 5) {
            // call api
            TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenIdempiereRespDto.getToken());


            try {
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
                url.append(commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRO)).append("?");
                url.append("$top=100");
                url.append("&$filter=AD_Client_ID eq ").append(tokenIdempiereRespDto.getAdClientId());// .append(" AND contains(Name,'Thịt')");
                if (credential.getIntType().equals("UPDATE"))
                    url.append(" AND Updated_Date ge '").append(commonService.castDateYYYYDDMM(credential)).append("'");
                url.append(" AND  startswith(image_product,'/')");
                url.append("&$skip=").append(0);
                url.append("&$select=M_Product_ID,value,name,description,isstocked,issold,ispurchased, isbundle, iscom, isbuffet, isbom, producttype,tax_category_object, UOM, product_category_object,assign_org_object, product_location_object,bom_object"); // image_product
                ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String responseBody = response.getBody();
                    try {
                        JsonNode root = objectMapper.readTree(responseBody);
                        JsonNode records = root.get("records");

                        if (records != null) {
                            List<ProductIntDto> productInt = this.castProductInt(records);
//                            syncProduct(productInt, credential);
                            InterfaceIntegration intSave = InterfaceIntegration.builder()
                                    .data(records)
                                    .type(AppConstant.DataTypeInt.PRODUCT)
                                    .status("NEW")
                                    .build();
                            this.interfaceIntegrationRepository.saveAndFlush(intSave);
                        }
                    } catch (Exception e) {
                        resultSync = "FAI";
                        e.printStackTrace();
                        log.error("Error when get data from erp: " + e.getMessage());
                    }

                }

                // end if
            } catch (Exception e) {
                resultSync = "FAI";
                e.printStackTrace();
                log.error("Error when get data from erp: " + e.getMessage());
            }

        }
//        if (credential.getSyncKafka().equals("Y")) {
//            try {
//                log.info("send message to kafka end page");
//                ProductIntKafkaDto productIntKafkaDto = ProductIntKafkaDto.builder()
//                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                        .productIntDtoList(new ArrayList<>())
//                        .syncIntegrationCredential(credential)
//                        .lastPage("Y")
//                        .build();
//                kafkaTemplate.send(TOPIC, productIntKafkaDto); // ban kafka lan 2
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return 0;
    }


    @Transactional
    public GlobalReponse syncProduct(List<ProductIntDto> param, SyncIntegrationCredential credential) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ProductIntDto>> requestEntity = new HttpEntity<>(param, headers);

//        if (credential.getIntType().equals("UPDATE") && credential.getSyncKafka().equals("N")) {
        return restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRODUCT, requestEntity, GlobalReponse.class);

//
//        return "INP";
    }

    public List<ProductCategoryDto> getProductCategoryIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp **");
        List<ProductCategoryDto> listProductCate = new ArrayList<>();
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntProductCategoryCredential intProductCatCredential = IntProductCategoryCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.commonIntegrationService.getTokenIdempiere().getToken());


        try {
            HttpEntity<IntProductCategoryCredential> requestEntity = new HttpEntity<>(intProductCatCredential, headers);
            String url = erpIntegration.getErpUrl() + this.commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PC);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {
                        List<PcTerminalAccessDto> listPcTerminal = new ArrayList<>();
                        JsonNode details = node.path("details");
                        for (JsonNode detail : details) {
                            PcTerminalAccessDto pcTerminalAccessDto = PcTerminalAccessDto.builder()
                                    .orgId(detail.path("AD_Org_ID").asInt())
                                    .isActive(detail.path("isActive").asText())
                                    .posTerminalId(detail.path("C_POS_ID").asInt())
                                    .build();
                            listPcTerminal.add(pcTerminalAccessDto);
                        }

                        ProductCategoryDto pcDto = ProductCategoryDto.builder()
                                .productCategoryParentId(node.path("M_Product_Category_Parent_ID").asInt())
                                //.isDefault
                                //.isSelfService
                                .orgId(0)
                                .name(node.path("name").asText())
                                .erpProductCategoryId(node.path("M_Product_Category_ID").asInt())
                                .isActive(node.path("isActive").asText())
                                .code(node.path("value").asText())
                                .pcTerminalAccesses(listPcTerminal)
                                .isSummary(node.path("isSummary").asText())
                                .isMenu(node.path("IsPosCate").asText())
                                .build();

                        listProductCate.add(pcDto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }

        return listProductCate;
    }

    public List<ProductCategoryDto> getProductCategoryERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getProductCategory List **");
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        credential.setOutputFormatter("yyyy-MM-dd");
        credential.setSyncKafka("N");
        IntUserCredential intUserCredential = IntUserCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.add("Cookie", commonIntegrationService.getCookie(",", "=", ""));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String url = tokenERPNextRespDto.getUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_RESOURCE) + "/" + AppConstant.ERPNextDoctype.D_ITEM_GROUP;
        StringBuilder paramFilter = new StringBuilder("[");
        paramFilter.append("[\"is_voucher\", \"=\", \"0\"]");
        if (credential.getIntType().equals("UPDATE"))
            paramFilter.append(",[\"modified\", \">=\", \"").append(credential.getIntDate()).append("\"]");
        paramFilter.append("]");
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("fields", "[\"*\"]")
                .queryParam("filters", paramFilter.toString())
                .queryParam("limit_page_length", 1000)
                .queryParam("order_by", "creation asc")
                .queryParam("limit_start", 0);
        List<ProductCategoryDto> listProductCate = new ArrayList<>();
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        try {
            responseErpNext = externalRestTemplate.exchange(uri.toUriString(), HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("data");
                for (JsonNode node : data) {
                    ProductCategoryDto productCategoryDto = ProductCategoryDto.builder()
                            .erpProductCategoryName(node.path("name").asText())
                            .code(node.path("name").asText())
                            .name(node.path("item_group_name").asText())
                            .indexSequence(BigDecimal.valueOf(node.path("index_sequence").asInt(0)))
                            .erpParentProductCat(node.path("parent_item_group").asText(""))
                            .build();
                    listProductCate.add(productCategoryDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        return listProductCate;
    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_INTERNAL_PROD, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageCAV(ConsumerRecord<String, ProductIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafla listener product");
        ProductIntKafkaDto value = consumerRecord.value();
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
                "0", 0, "vi-VN", value.getTenantId()));

        try {
            List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.PRODUCT, "NEW");
            String STATUS = "FAI";
            int i = 0;
            for (InterfaceIntegration itemP : interfaceInt) {
                List<ProductIntDto> listInt = this.castProductInt(itemP.getData());
                STATUS = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRODUCT, listInt, GlobalReponse.class).getStatus() == HttpStatus.OK.value() ? "COM" : "FAI";
                interfaceIntegrationRepository.delete(itemP);
                interfaceInt.remove(itemP);
                i++;
                if (i > 5 || !interfaceInt.isEmpty()) {
                    this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_INTERNAL_PROD, value);
                }

            }
            if (interfaceInt.isEmpty()) {
                IntegrationHistory integrationHistory = integrationHistoryRepository.findById(value.getSyncIntegrationCredential().getIntegrationHistoryId()).orElse(null);
                if (integrationHistory != null) {
                    integrationHistory.setIntStatus("COM");
                    this.integrationHistoryRepository.save(integrationHistory);
                } else {
                    log.info("not found integration history integration customer - vendor - partner group");
                }
            }
        } catch (Exception e) {
            acknowledgment.acknowledge();
        }

    }

    public List<ProductIntDto> castProductInt(JsonNode records) {
        List<ProductIntDto> productInt = new ArrayList<>();
        for (JsonNode record : records) {

            // hang che bien = iscombo  PRD
            // combo - dong goi = isbunble CBP
            // iscom (auxiliar) la thanh phan

            ProductIntDto prod = ProductIntDto.builder()
                    .code(record.get("Value").asText())
                    .name(record.get("Name").asText())
                    .description(record.has("Description") ? record.get("Description").asText() : "")
                    .isPurchased(record.get("IsPurchased").asBoolean() ? "Y" : "N")
                    .isSales(record.get("IsSold").asBoolean() ? "Y" : "N")
                    .isStocked(record.get("IsStocked").asBoolean() ? "Y" : "N")
                    .erpProductId(record.get("id").asInt())
                    .build();
//                            if(record.get("ProductType").asText().equals("S"))
//                                prod.setProductType("SVC");
//                            else
//                                prod.setProductType("RGD");

            if (record.get("IsBundle").asBoolean())
                prod.setGroupType("CBP");
            else if (record.get("IsBOM").asBoolean())
                prod.setGroupType("PRD");
            else
                prod.setGroupType("RGD");

            if (record.get("IsCOM").asBoolean())
                prod.setIsTopping("N");

            if (record.has("image_product")) {
                ImageDto imageDto = ImageDto.builder()
                        .image64(record.get("image_product").asText())
                        .build();
                prod.setImage(imageDto);
            }
            // xu ly uom ,tax category, product category
            String uomJson = record.has("uom") ? record.get("uom").asText() : null;
            if (uomJson != null) {
                JSONObject uomJsonObject = new JSONObject(uomJson);
                UomDto uomDto = UomDto.builder()
                        .erpUomId(uomJsonObject.getInt("c_uom_id"))
                        .name(uomJsonObject.getString("name"))
                        .code(uomJsonObject.getString("code"))
                        .build();
                prod.setUom(uomDto);
            }

            String taxCategoryJson = record.has("tax_category_object") ? record.get("tax_category_object").asText() : null;
            if (taxCategoryJson != null) {
                JSONObject taxCategoryJsonOb = new JSONObject(taxCategoryJson);
                TaxDto taxDto = TaxDto.builder()
                        .erpTaxId(taxCategoryJsonOb.getInt("c_tax_id"))
                        .name(taxCategoryJsonOb.getString("name"))
                        .taxRate(taxCategoryJsonOb.getBigDecimal("rate"))
                        .build();
                TaxCategoryIntDto taxCategoryDto = TaxCategoryIntDto.builder()
                        .erpTaxCategoryId(taxCategoryJsonOb.getInt("c_taxcategory_id"))
                        .name(taxCategoryJsonOb.has("taxcategory_name") ? taxCategoryJsonOb.getString("taxcategory_name") : " ")
                        .tax(taxDto)
                        .build();
                prod.setTaxCategory(taxCategoryDto);
            }
            String productCategory = record.has("product_category_object") ? record.get("product_category_object").asText() : null;
            if (productCategory != null) {
                JSONObject productCategoryJSon = new JSONObject(productCategory);
                ProductCategoryDto productCategoryDto = ProductCategoryDto.builder()
                        .erpProductCategoryId(productCategoryJSon.getInt("m_product_category_id"))
                        .code(productCategoryJSon.getString("value"))
                        .name(productCategoryJSon.getString("name"))
                        .isMenu("Y")
                        .build();
                prod.setProductCategory(productCategoryDto);
            }
            // assign org
            String assignOrgObject = record.has("assign_org_object") ? record.get("assign_org_object").asText() : null;
            if (assignOrgObject != null) {
                JSONArray assignOrgObjectJson = new JSONArray(assignOrgObject);
                List<AssignOrgProductDto> listAssignOrg = new ArrayList<>();
                for (int i = 0; i < assignOrgObjectJson.length(); i++) {
                    JSONObject item = assignOrgObjectJson.getJSONObject(i);
                    AssignOrgProductDto assignOrgProductDto = AssignOrgProductDto.builder()
                            .orgId(item.getInt("ad_org_id"))
                            .build();
                    listAssignOrg.add(assignOrgProductDto);
                }
                ;
                prod.setAssignOrg(listAssignOrg);
            }
            String productLocationObject = record.has("product_location_object") ? record.get("product_location_object").asText() : null;
            if (productLocationObject != null) {
                JSONArray productLocationObjectJson = new JSONArray(productLocationObject);
                List<ProductLocationDto> listProductLocations = new ArrayList<>();
                for (int i = 0; i < productLocationObjectJson.length(); i++) {
                    JSONObject item = productLocationObjectJson.getJSONObject(i);
                    ProductLocationDto productLocationDto = ProductLocationDto.builder()
                            .orgId(item.getInt("ad_org_id"))
                            .warehouseId(item.getInt("m_warehouse_id"))
                            .isActive(item.getString("isactive"))
                            .posTerminalId(item.optInt("c_pos_id", 0))
                            .isDefault("Y")
                            .build();
                    listProductLocations.add(productLocationDto);
                }
                ;
                prod.setProductLocation(listProductLocations);
            }
            // xu ly bom
            String productComboObject = record.has("bom_object") ? record.get("bom_object").asText() : null;
            if (productComboObject != null) {
                JSONArray productComboObjectJson = new JSONArray(productComboObject);
                List<ProductComboIntDto> listProductCombo = new ArrayList<>();
                BigDecimal preparationTime = new BigDecimal(0);
                BigDecimal cookingTime = new BigDecimal(0);
                for (int i = 0; i < productComboObjectJson.length(); i++) {
                    JSONObject item = productComboObjectJson.getJSONObject(i);
                    ProductComboIntDto productComboDto = ProductComboIntDto.builder()
                            .productComponentId(Integer.valueOf(String.valueOf(item.get("m_product_id"))))
                            .qty(new BigDecimal(String.valueOf(item.get("qtybom"))))
                            .sequence(Integer.valueOf(String.valueOf(item.get("line"))))
                            .build();
                    if (item.has("preparation_time") && !item.isNull("preparation_time")) {

                            preparationTime = item.getBigDecimal("preparation_time");
                    }
                    if (item.has("cooking_time") && !item.isNull("preparation_time")) {
                        cookingTime = item.getBigDecimal("cooking_time");
                    }
                    listProductCombo.add(productComboDto);
                }
                ;
                prod.setPreparationTime(preparationTime);
                prod.setCookingTime(cookingTime);
                prod.setProductCombo(listProductCombo);
            }
            productInt.add(prod);
        }
        return productInt;
    }


    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_INTERNAL_PRODUCT, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageProduct(ConsumerRecord<String, SyncIntegrationCredential> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafka listener internal product");
        SyncIntegrationCredential value = consumerRecord.value();
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
        try {

//        value.setLineProduct(value.getLineProduct()+5);
            List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.PRODUCT, "NEW");
            String STATUS = "FAI";
            Iterator<InterfaceIntegration> iterator = interfaceInt.iterator();
            while (iterator.hasNext()) {
                InterfaceIntegration itemP = iterator.next();
                List<ProductIntDto> listInt = this.castProductInt(itemP.getData());
                STATUS = this.commonIntegrationService.sendPostRequest(
                        AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_INTEGRATION_PRODUCT,
                        listInt, GlobalReponse.class
                ).getStatus() == HttpStatus.OK.value() ? "COM" : "FAI";
                interfaceIntegrationRepository.delete(itemP);
                iterator.remove();
            }
            kafkaTemplate.send(AppConstant.TopicKafka.TPROD, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }
    }


    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TPROD, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageProduct3(ConsumerRecord<String, SyncIntegrationCredential> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafka listener internal product");
        SyncIntegrationCredential value = consumerRecord.value();
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
        try {
            AuditContext.setAuditInfo(new AuditInfo(0, 0, "0",
                    "0", 0, "vi", value.getTenantId()));

//        value.setLineProduct(value.getLineProduct()+5);
            value.setError("OK");
            IntegrationHistoryInfoDto result = this.getProductErp(value);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }
    }


    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SEND_SYNC_IMAGE_PRODUCT, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageImage(ConsumerRecord<String, SyncIntegrationCredential> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafka listener internal product");
        SyncIntegrationCredential value = consumerRecord.value();
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
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        List<Integer> listIdProduct = new ArrayList<>();
        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
            url.append(commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRO)).append("?");
            url.append("$top=100");
            url.append("&$filter=AD_Client_ID eq ").append(tokenIdempiereRespDto.getAdClientId());//.append(" and contains(image_product,'/') and IsStocked eq true and IsBundle eq false ");// .append(" AND contains(Name,'Thịt')");
            if (value.getIntType().equals("UPDATE"))
                url.append(" AND Updated_Date ge '").append(commonService.castDateYYYYDDMM(value)).append("'");
            url.append(" AND startswith(image_product,'/')");
            url.append("&$skip=" + 0);
            url.append("&$select=image_product");//image_product

            payload.append(url.toString());
            ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {

                String responseBody = response.getBody();
                responseGlobal.append(responseBody);
                try {
                    JsonNode root = objectMapper.readTree(responseBody);
                    Integer pageCount = root.get("page-count").asInt();
                    Integer recordsSize = root.get("records-size").asInt();
                    Integer skipRecords = root.get("skip-records").asInt();
                    Integer rowCount = root.get("row-count").asInt();
                    JsonNode records = root.get("records");
                    for (JsonNode node : records) {
                        listIdProduct.add(node.get("id").intValue());
                    }
                    for (int i = 100; i <= rowCount; i = i + 100) {
                        url = new StringBuilder(tokenIdempiereRespDto.getUrl());
                        url.append(commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRO)).append("?");
                        url.append("$top=100");
                        url.append("&$filter=AD_Client_ID eq ").append(tokenIdempiereRespDto.getAdClientId());//.append(" and contains(image_product,'/') and IsStocked eq true and IsBundle eq false ");// .append(" AND contains(Name,'Thịt')");
                        if (value.getIntType().equals("UPDATE"))
                            url.append(" AND Updated_Date ge '").append(commonService.castDateYYYYDDMM(value)).append("'");
                        url.append("&$skip=" + i);
                        url.append("&$select=image_product");//image_product
                        if (response.getStatusCode().is2xxSuccessful()) {
                            JsonNode data = root.get("records");
                            for (JsonNode node : data) {
                                listIdProduct.add(node.get("id").intValue());
                            }
                        }
                    }
                    InterfaceIntegration intSync = InterfaceIntegration.builder()
                            .data(objectMapper.valueToTree(listIdProduct))
                            .type(AppConstant.DataTypeInt.IMAGE_PRODUCT)
                            .status(AppConstant.IntStatus.NEW)
                            .build();
                    this.interfaceIntegrationRepository.save(intSync);
                    ListSyncProduct listSync = ListSyncProduct.builder()
                            .listProduct(listIdProduct)
                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                            .build();
                    if (!listIdProduct.isEmpty())
                        this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SAVE_IMAGE_PRODUCT, listSync);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }

    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SAVE_IMAGE_PRODUCT, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageSaveImage(ConsumerRecord<String, ListSyncProduct> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafka listener Synch image product");
        ListSyncProduct value = consumerRecord.value();
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
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        try {
            Iterator<Integer> listProduct = value.getListProduct().iterator();
            while (listProduct.hasNext()) {
                Integer currentId = listProduct.next();
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
                url.append(commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_PRO)).append("?");
                url.append("$top=100");
                url.append("&$filter=M_Product_ID eq ").append(currentId);//.append(" and contains(image_product,'/') and IsStocked eq true and IsBundle eq false ");// .append(" AND contains(Name,'Thịt')");
                url.append("&$select=image_product");//image_product
                ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    String responseBody = response.getBody();
                    responseGlobal.append(responseBody);
                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode records = root.get("records");
                    for (JsonNode j : records) {
                        ProductDto productSaveImg = ProductDto.builder()
                                .image(ImageDto.builder()
                                        .image64(j.get("image_product").asText())
                                        .imageCode("").build()).id(currentId).build();
                        this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_API_SAVE_IMAGE, productSaveImg, GlobalReponse.class);
                    }
                }
                listProduct.remove();
                if (listProduct.hasNext()) {
                    Iterable<Integer> iterable = () -> listProduct;
                    List<Integer> param = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
                    ListSyncProduct paramSync = ListSyncProduct.builder()
                            .listProduct(param)
                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                            .build();
                    this.kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SAVE_IMAGE_PRODUCT, paramSync);
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
