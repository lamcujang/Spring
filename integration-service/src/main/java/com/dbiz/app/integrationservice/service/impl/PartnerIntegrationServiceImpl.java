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
import com.dbiz.app.integrationservice.service.PartnerIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.CommonService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.IntPartnerDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.dto.userDto.VendorDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPartnerCredential;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PartnerIntegrationServiceImpl implements PartnerIntegrationService {
    private final ErpIntegrationRepository erpIntegrationRepository;

    private final MessageSource messageSource;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final IntegrationHistoryRepository integrationHistoryRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final InterfaceIntegrationRepository interfaceIntegrationRepository;

    private final DataSourceContextHolder dataSourceContextHolder;

    private final DataSourceConfigService dataSourceConfigService;

    private final DataSourceRoutingService dataSourceRoutingService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional
    public IntegrationHistory partnerIntegration(SyncIntegrationCredential credential) {
        StringBuilder error = new StringBuilder();
        log.info("**ServiceImpl::layer -  partner integration **");
        StringBuilder message = new StringBuilder();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        GlobalReponse response = new GlobalReponse();
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        IntPartnerDto getPartnerErp = null;
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS))
            getPartnerErp = getBpartnerIdempiere(credential, erpIntegration);
        else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS))
            getPartnerErp = getBpartnerERPNext(credential, erpIntegration);

        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        StringBuilder responseJson = new StringBuilder(" **  save partner group ** \n");
        // dong bo partner group erpnext  to pos
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            log.info("** case erpnext to pos **");
            StringBuilder messageErpNext = new StringBuilder("");
            StringBuilder errorErpNext = new StringBuilder("");
            IntegrationHistory intReturn = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .userId(credential.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(0)
                    .id(credential.getIntegrationHistoryId())
                    .build();
            if (getPartnerErp.getSendKafka().equals("N")) // khong sync kafka
            {
                HttpEntity<List<PartnerGroupDto>> requestEntity = new HttpEntity<>(getPartnerErp.getPartnerGroups(), headers);
                response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_PARTNER_GROUP_ERPNEXT, requestEntity, GlobalReponse.class);
                if (response.getStatus().equals(HttpStatus.OK.value()))
                    messageErpNext.append(response.getMessage());
                else
                    errorErpNext.append(response.getErrors());

                HttpEntity<List<CustomerDto>> requestCustomer = new HttpEntity<>(getPartnerErp.getCustomers(), headers);
                response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_CUSTOMMER_ERPNEXT, requestCustomer, GlobalReponse.class);
                if (response.getStatus().equals(HttpStatus.OK.value()))
                    messageErpNext.append(response.getMessage());
                else
                    errorErpNext.append(response.getErrors());
                intReturn.setDescription(errorErpNext.toString() + " \n " + messageErpNext.toString());
                intReturn.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
                return intReturn;
            } else {
                intReturn.setDescription(errorErpNext.toString() + " \n " + messageErpNext.toString());
                intReturn.setIntStatus("INP");
                return intReturn;
            }
        }
        // dong bo partner group idempiere to pos
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            if (credential.getIntType().equals("UPDATE") || getPartnerErp.getSendKafka().equals("N")) {
                HttpEntity<List<PartnerGroupDto>> requestEntity = new HttpEntity<>(getPartnerErp.getPartnerGroups(), headers);
                response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_PARTNER_GROUP, requestEntity, GlobalReponse.class);
                message.append(response.getErrors());
                responseJson.append(Json.pretty(response));
                responseJson.append(" ** save customer ** \n");
                // dong bo customer
                HttpEntity<List<CustomerDto>> requestCustomer = new HttpEntity<>(getPartnerErp.getCustomers(), headers);
                response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_CUSTOMMER, requestCustomer, GlobalReponse.class);
                message.append(response.getErrors());
                responseJson.append(Json.pretty(response));
                responseJson.append(" ** save vendor ** \n");
                // dong  bo vendor

                HttpEntity<List<VendorDto>> requestVendor = new HttpEntity<>(getPartnerErp.getVendors(), headers);
                response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_VENDOR, requestVendor, GlobalReponse.class);
                responseJson.append(Json.pretty(response));
                message.append(response.getErrors());
                if (response.getStatus() != HttpStatus.OK.value()) {
                    message = new StringBuilder(response.getErrors());
                }
            }

            IntegrationHistory intReturn = IntegrationHistory.builder()
                    .intType(credential.getDataType())
                    .intFlow(credential.getIntFlow())
                    .intDate(Instant.now())
                    .userId(credential.getUserId())
                    .tenantId(AuditContext.getAuditInfo().getTenantId())
                    .orgId(0)
                    .id(credential.getIntegrationHistoryId())
                    .build();
            if (credential.getIntType().equals("INSERT") && getPartnerErp.getSendKafka().equals("Y")) {
                intReturn.setIntStatus("INP");

            } else {
                intReturn.setIntStatus(response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI");
            }
            return intReturn;
        }
        return null;
    }

    /**
     *
     * @param erpIntegration
     * @return
     */
    @Override // test khong sai
    public GlobalReponse getPartnerERP(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        GlobalReponse response = new GlobalReponse();
        log.info("** get partner INT ERPNext **");
        IntPartnerDto partnerResponse = new IntPartnerDto();
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        partnerResponse.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        partnerResponse.setSyncIntegrationCredential(credential);
        partnerResponse.setSendKafka("N");
        // - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntPartnerCredential intPartnerCredential = IntPartnerCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.add("Cookie", commonIntegrationService.getCookie(",", "=", ""));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        StringBuilder url = new StringBuilder(tokenERPNextRespDto.getUrl() + "/api/method/" + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_CUSTOMER));
        log.info("url integration customer: " + url.toString());

        StringBuilder param = new StringBuilder("filters=[[\"customer_type\", \"=\", \"Individual\"]]&");
        param.append("limit_page_length=20&limit_start=0");
        if (credential.getIntType().equals("INSERT"))
            url.append("?").append(param.toString());
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<PartnerGroupDto> listPartnerDto = new ArrayList<>();
        List<CustomerDto> listCustomerDto = new ArrayList<>();
        try {
            responseErpNext = externalRestTemplate.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("message").path("data");
                for (JsonNode item : data) {
                    JsonNode nodePartnerGroup = item.path("customer_group");
                    if (nodePartnerGroup != null && !nodePartnerGroup.isEmpty()) {
                        PartnerGroupDto itemPartner = new PartnerGroupDto();
                        if (nodePartnerGroup.get("parent_customer_group") != null && !nodePartnerGroup.get("parent_customer_group").isEmpty()) {
                            PartnerGroupDto parent = new PartnerGroupDto();
                            parent.setErpBpGroupName(nodePartnerGroup.get("parent_customer_group").path("name").asText());
                            parent.setCompany(nodePartnerGroup.get("parent_customer_group").path("company").asText());
                            parent.setId(nodePartnerGroup.get("parent_customer_group").path("pos_customer_group_id").asInt());
                            parent.setGroupName(nodePartnerGroup.get("parent_customer_group").path("customer_group_name").asText());
                            parent.setGroupCode(nodePartnerGroup.get("parent_customer_group").path("name").asText());
                            itemPartner.setParent(parent);
                        }
                        itemPartner.setErpBpGroupName(nodePartnerGroup.path("name").asText());
                        itemPartner.setCompany(nodePartnerGroup.path("company").asText());
                        itemPartner.setId(nodePartnerGroup.path("pos_customer_group_id").asInt());
                        itemPartner.setGroupName(nodePartnerGroup.path("customer_group_name").asText());
                        itemPartner.setGroupCode(nodePartnerGroup.path("name").asText());
                        listPartnerDto.add(itemPartner);
                    }
                    CustomerDto cusInt = new CustomerDto();
                    cusInt.setName(item.path("customer_name").asText());
                    cusInt.setCode(item.path("name").asText());
                    if (item.path("gender") != null)
                        if (!item.path("gender").asText().equals("Female") && !item.path("gender").asText().equals("Male"))
                            cusInt.setGender("OTH");
                        else
                            cusInt.setGender(item.path("gender").asText().equals("Female") ? "FEM" : "MAL");
                    cusInt.setErpCustomerName(item.path("name").asText());
                    if (item.path("contact") != null && !item.path("contact").isEmpty()) {
                        cusInt.setPhone1(item.path("contact").path("mobile_no").asText());
                        cusInt.setEmail(item.path("contact").path("email_id").asText());
                    }
                    if (item.path("address") != null && !item.path("address").isEmpty()) {
                        cusInt.setAddress1(item.path("address").path("address_title").asText());
                    }
                    listCustomerDto.add(cusInt);
                }
            }
            String json = objectMapper.writeValueAsString(listPartnerDto);
            log.info("response from : " + json);
            response.setData(listCustomerDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        return response;
    }

    @Transactional
    // get partner from erp erpnext
    public IntPartnerDto getBpartnerERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** get partner INT ERPNext **");
        credential.setOutputFormatter("yyyy-MM-dd");
        IntPartnerDto partnerResponse = new IntPartnerDto();
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        partnerResponse.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        partnerResponse.setSyncIntegrationCredential(credential);
        partnerResponse.setSendKafka("N");
        // - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntPartnerCredential intPartnerCredential = IntPartnerCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.add("Cookie", commonIntegrationService.getCookie(",", "=", ""));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        StringBuilder url = new StringBuilder(tokenERPNextRespDto.getUrl() + "/api/method/" + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_CUSTOMER));
        URI uri = UriComponentsBuilder.fromHttpUrl(url.toString())
                .queryParam("type", intPartnerCredential.getIntType())
                .queryParam("date", intPartnerCredential.getIntDate())
                .build().toUri();
        log.info("url integration customer: " + url.toString());
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        Set<PartnerGroupDto> uniquePartnerGroup = new HashSet<>();
        List<CustomerDto> listCustomerDto = new ArrayList<>();
        try {
            responseErpNext = externalRestTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                int size = root.path("message").path("totalItem").asInt(0);
                JsonNode data = root.path("message").path("data");
                if (size > 100) {
                    List<JsonNode> jsonObjects = new ArrayList<>();
                    data.forEach(jsonObjects::add);
                    int batchSize = 200;
                    List<List<JsonNode>> batches = new ArrayList<>();
                    for (int j = 0; j < jsonObjects.size(); j += batchSize) {
                        int end = Math.min(j + batchSize, jsonObjects.size());
                        batches.add(jsonObjects.subList(j, end));
                    }
                    for (int j = 0; j < batches.size(); j++) {
                        ArrayNode arrayNode = objectMapper.createArrayNode();
                        arrayNode.addAll(batches.get(j));
                        InterfaceIntegration interfacePartner = InterfaceIntegration.builder()
                                .data(arrayNode)
                                .type(AppConstant.DataTypeInt.CUSTOMER_VENDOR)
                                .status("NEW")
                                .build();
                        interfaceIntegrationRepository.saveAndFlush(interfacePartner);
                    }
                    partnerResponse.setSendKafka("Y");
                } else if (size < 100 && size > 20) {
                    InterfaceIntegration interfacePartner = InterfaceIntegration.builder()
                            .data(data)
                            .type(AppConstant.DataTypeInt.CUSTOMER_VENDOR)
                            .status("NEW")
                            .build();
                    interfaceIntegrationRepository.saveAndFlush(interfacePartner);
                    partnerResponse.setSendKafka("Y");
                } else {
                    for (JsonNode item : data) {
                        CustomerDto cusInt = castJsonToCustomerDto(item);
                        if (cusInt.getPartnerGroup() != null)
                            uniquePartnerGroup.add(cusInt.getPartnerGroup());
                        listCustomerDto.add(cusInt);
                    }
                    List<PartnerGroupDto> listPartnerDto = new ArrayList<>(uniquePartnerGroup);
                    partnerResponse.setPartnerGroups(listPartnerDto);
                    partnerResponse.setCustomers(listCustomerDto);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }
        if (partnerResponse.getSendKafka().equals("Y")) {
            partnerResponse.setPlatform(AppConstant.ERP_PLATFORM_ERPNEXT);
            eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_SEND_CAV_INT, partnerResponse));
        }
//            eventPublisher.publishEvent(new KafkaSendEvent(this, partnerResponse));
        return partnerResponse;
    }

    // get partner from erp idempiere
    @Transactional
    public IntPartnerDto getBpartnerIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** get partner int Idempiere **");
        IntPartnerDto partnerResponse = new IntPartnerDto();
        partnerResponse.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
        partnerResponse.setSyncIntegrationCredential(credential);
        partnerResponse.setSendKafka("N");
        boolean sendPartner = false;
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntPartnerCredential intPartnerCredential = IntPartnerCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(commonIntegrationService.getTokenIdempiere().getToken());

        try {
            HttpEntity<IntPartnerCredential> requestEntity = new HttpEntity<>(intPartnerCredential, headers);
            String url = erpIntegration.getErpUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_BPN);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    List<CustomerDto> customerDtos = new ArrayList<>();
                    List<VendorDto> vendorDtos = new ArrayList<>();
                    List<PartnerGroupDto> partnerGroupDtos = new ArrayList<>();
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {
                        // lay partner group
                        PartnerGroupDto group = PartnerGroupDto.builder()
                                .groupName(node.path("name").asText())
                                .isActive(node.path("isActive").asText())
                                .groupCode(node.path("value").asText())
                                .erpBpGroupId(node.path("C_BP_Group_ID").asInt())
                                .isCustomer(node.path("isCustomer").asText())
                                .build();
                        partnerGroupDtos.add(group);
                    }
                    boolean shouldBreak = false;
                    for (JsonNode node : root.path("data")) {
                        if (node.get("isCustomer").asText().equals("Y")) {
                            JsonNode listCustomer = node.path("partner");
                            for (JsonNode item : listCustomer) {
                                CustomerDto customerDto = CustomerDto.builder()
//                                        .name(item.path("userName").asText() + " " + item.path("partnerName").asText())
                                        .name(item.path("partnerName").asText())
                                        .discount(item.path("flatDiscount").decimalValue())
                                        .erpCustomerId(item.path("C_BPartner_ID").asInt())
                                        .isActive(item.path("isActive").asText())
                                        .code(item.path("value").asText())
                                        .partnerGroupId(item.path("C_BP_Group_ID").asInt())
                                        .phone1(item.path("phone").asText())
                                        .isPosVip(item.path("isPOSVIP").asText())
                                        .partnerName(item.path("partnerName").asText())
                                        .taxCode(item.path("taxId").asText())
                                        .email(item.path("email").asText())
                                        .creditLimit(item.path("creaditLimit").decimalValue())
                                        .debitAmount(item.path("debt").decimalValue())
                                        .gender(item.path("gender").asText())
                                        .build();
                                if (item.path("SOCreditStatus").asText().equals("O")) {
                                    customerDto.setIsDebt("Y");
                                }
                                if (item.path("SOCreditStatus").asText().equals("X")) {
                                    customerDto.setIsDebt("N");
                                }
                                if (item.get("Birthday") != null) {
                                    customerDto.setBirthday(DateHelper.toLocalDate(item.path("Birthday").asText()));
                                }
                                if (item.path("address") != null) {
                                    String address = item.path("address").asText().replace("+", " ");
                                    customerDto.setAddress1(address);
                                }
                                customerDtos.add(customerDto);
                                if (customerDtos.size() == 20 && credential.getIntType().equals("INSERT")) {
                                    partnerResponse.setSendKafka("Y");
                                    partnerResponse.setPartnerGroups(partnerGroupDtos);
                                    partnerResponse.setCustomers(customerDtos);
                                    partnerResponse.setPartnerGroups(new ArrayList<>());
                                    partnerResponse.setCustomers(new ArrayList<>());
                                    customerDtos = new ArrayList<>();
                                    partnerGroupDtos = new ArrayList<>();
                                    JsonNode data = root.path("data");
                                    ArrayNode partnerGroup = objectMapper.createArrayNode();
                                    for (JsonNode i : data) {
                                        ObjectNode partnerJson = objectMapper.createObjectNode();
                                        partnerJson.put("isCustomer", i.path("isCustomer").asText());
                                        partnerJson.put("name", i.path("name").asText());
                                        partnerJson.put("isActive", i.path("isActive").asText());
                                        partnerJson.put("value", i.path("value").asText());
                                        partnerJson.put("C_BP_Group_ID", i.path("C_BP_Group_ID").asInt());
                                        partnerGroup.add(partnerJson);
                                        if (partnerGroup.size() >= 10) {
                                            InterfaceIntegration itnPGroup = InterfaceIntegration.builder()
                                                    .data(partnerGroup)
                                                    .dataText(objectMapper.writeValueAsString(partnerGroup))
                                                    .type(AppConstant.DataTypeInt.BPARTNER_GROUP)
                                                    .status(AppConstant.IntStatus.NEW)
                                                    .build();
                                            this.interfaceIntegrationRepository.save(itnPGroup);
                                            partnerGroup = objectMapper.createArrayNode();
                                        }
                                    }
                                    if (!partnerGroup.isEmpty()) {
                                        InterfaceIntegration itnPGroup = InterfaceIntegration.builder()
                                                .data(partnerGroup)
                                                .dataText(objectMapper.writeValueAsString(partnerGroup))
                                                .type(AppConstant.DataTypeInt.BPARTNER_GROUP)
                                                .status(AppConstant.IntStatus.NEW)
                                                .build();
                                        this.interfaceIntegrationRepository.save(itnPGroup);
                                    }
                                    ArrayNode customerList = objectMapper.createArrayNode();
                                    for (JsonNode i : data) {
                                        JsonNode partner = i.path("partner");
                                        for (JsonNode iP : partner) {
                                            ObjectNode customerJson = objectMapper.createObjectNode();
                                            customerJson = (ObjectNode) iP;
                                            customerList.add(customerJson);
                                            if (customerList.size() >= 10) {
                                                InterfaceIntegration itnCus = InterfaceIntegration.builder()
                                                        .data(customerList)
                                                        .dataText(objectMapper.writeValueAsString(customerList))
                                                        .type(i.path("isCustomer").asText().equals("Y") ? AppConstant.DataTypeInt.CUSTOMER : AppConstant.DataTypeInt.VENDOR)
                                                        .status(AppConstant.IntStatus.NEW)
                                                        .build();
                                                this.interfaceIntegrationRepository.save(itnCus);
                                                customerList = objectMapper.createArrayNode();
                                            }
                                        }
                                        if (!customerList.isEmpty()) {
                                            InterfaceIntegration itnCus = InterfaceIntegration.builder()
                                                    .data(customerList)
                                                    .dataText(objectMapper.writeValueAsString(customerList))
                                                    .type(i.path("isCustomer").asText().equals("Y") ? AppConstant.DataTypeInt.CUSTOMER : AppConstant.DataTypeInt.VENDOR)
                                                    .status(AppConstant.IntStatus.NEW)
                                                    .build();
                                            this.interfaceIntegrationRepository.save(itnCus);
                                        }
                                    }
                                    shouldBreak = true;
                                    break;
                                }

                            }

                        } else {
                            JsonNode listPartner = node.path("partner");
                            for (JsonNode item : listPartner) {
                                VendorDto vendorDto = VendorDto.builder()
                                        .name(item.path("partnerName").asText())
                                        .isActive(item.path("isActive").asText())
                                        .code(item.path("value").asText())
                                        .partnerName(item.path("partnerName").asText())
                                        .description(item.path("description").asText())
                                        .isPosVip(item.path("isPOSVIP").asText())
                                        .code(item.path("value").asText())
                                        .disCount(item.path("flatDiscount").decimalValue())
                                        .phone1(item.path("phone").asText())
                                        .partnerGroupId(item.path("C_BP_Group_ID").asInt())
                                        .erpVendorId(item.path("C_BPartner_ID").asInt())
                                        .taxCode(item.path("taxId").asText())
                                        .email(item.path("email").asText())
                                        .creditLimit(item.path("creaditLimit").decimalValue())
                                        .debitAmount(item.path("debt").decimalValue())
                                        .build();
                                vendorDtos.add(vendorDto);
                            }
                        }
                        if (shouldBreak) {
                            break;
                        }
                    }
                    partnerResponse.setPartnerGroups(partnerGroupDtos);
                    partnerResponse.setCustomers(customerDtos);
                    partnerResponse.setVendors(vendorDtos);
                    if (credential.getIntType().equals("INSERT") && partnerResponse.getSendKafka().equals("Y")) {
                        partnerResponse.setPlatform(AppConstant.ERP_PLATFORM_IDEMPIERE);
                        eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_SEND_CAV_INT, partnerResponse));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }

        return partnerResponse;
    }

    public CustomerDto castJsonToCustomerDto(JsonNode jsonNode) {
        CustomerDto cusInt = new CustomerDto();
        JsonNode nodePartnerGroup = jsonNode.path("p_customer_group_name");
        PartnerGroupDto itemPartner = null;
        if (nodePartnerGroup != null && !nodePartnerGroup.isEmpty()) {
            itemPartner = new PartnerGroupDto();
            if (jsonNode.get("parent_customer_group") != null && !jsonNode.get("parent_customer_group").isEmpty()) {
                PartnerGroupDto parent = new PartnerGroupDto();
                parent.setErpBpGroupName(jsonNode.get("parent_customer_group").asText());
                parent.setCompany(jsonNode.get("tcg2_company").asText());
                parent.setId(jsonNode.get("tcg2_pos_customer_group_id").asInt() == 0 ? null : jsonNode.get("tcg2_pos_customer_group_id").asInt());
                parent.setGroupName(jsonNode.get("tcg2_customer_group_name").asText());
                parent.setGroupCode(jsonNode.get("parent_customer_group").asText());
                itemPartner.setParent(parent);
            }
            itemPartner.setErpBpGroupName(jsonNode.path("p_customer_group_name").asText());
            itemPartner.setCompany(jsonNode.path("tcg_company").asText());
            itemPartner.setId(jsonNode.path("pos_customer_group_id").asInt() == 0 ? null : jsonNode.path("pos_customer_group_id").asInt());
            itemPartner.setGroupName(jsonNode.path("customer_group_name").asText());
            itemPartner.setGroupCode(jsonNode.path("p_customer_group_name").asText());

        }
        cusInt.setName(jsonNode.path("customer_name").asText());
        cusInt.setCode(jsonNode.path("p_customer_name").asText());
        if (!jsonNode.path("gender").asText().equals("Female") && !jsonNode.path("gender").asText().equals("Male"))
            cusInt.setGender("OTH");
        else
            cusInt.setGender(jsonNode.path("gender").asText().equals("Female") ? "FEM" : "MAL");
        cusInt.setErpCustomerName(jsonNode.path("p_customer_name").asText());
        cusInt.setPhone1((jsonNode.path("phone").asText(null)));
        cusInt.setEmail((jsonNode.path("email_id").asText(null)));
        cusInt.setAddress1((jsonNode.path("address_title").asText(null)));
        cusInt.setId(jsonNode.path("pos_customer_id").asInt() == 0 ? null : jsonNode.path("pos_customer_id").asInt());
        cusInt.setPartnerGroup(itemPartner);
        return cusInt;
    }


    // kafka listener erpnext
    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SEND_CAV_INT, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageCAV(ConsumerRecord<String, IntPartnerDto> consumerRecord, Acknowledgment acknowledgment) {
        int i = 0;
        log.info("kafla listener customer vendor");
        IntPartnerDto value = consumerRecord.value();
        String Status = "FAI";
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

        List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeInAndStatus(Arrays.asList(AppConstant.DataTypeInt.CUSTOMER, AppConstant.DataTypeInt.BPARTNER_GROUP, AppConstant.DataTypeInt.VENDOR), AppConstant.IntStatus.NEW);
        if (value.getPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT)) {
            Set<PartnerGroupDto> uniquePartnerGroup = new HashSet<>();
            List<CustomerDto> listCustomerDto = new ArrayList<>();
            IntPartnerDto partnerResponse = new IntPartnerDto();
            StringBuilder messageErpNext = new StringBuilder("");
            StringBuilder errorErpNext = new StringBuilder("");
            String STATUS = "FAI";
            for (InterfaceIntegration itemP : interfaceInt) {
                for (JsonNode item : itemP.getData()) {
                    CustomerDto cusInt = castJsonToCustomerDto(item);
                    if (cusInt.getPartnerGroup() != null)
                        uniquePartnerGroup.add(cusInt.getPartnerGroup());
                    listCustomerDto.add(cusInt);
                    List<PartnerGroupDto> listPartnerDto = new ArrayList<>(uniquePartnerGroup);
                    if (listCustomerDto.size() == 20) {
                        GlobalReponse response = new GlobalReponse();
                        response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_PARTNER_GROUP_ERPNEXT, listPartnerDto, GlobalReponse.class);
                        if (response.getStatus().equals(HttpStatus.OK.value())) {
                            messageErpNext.append(response.getMessage());
                            STATUS = "COM";
                        } else
                            errorErpNext.append(response.getErrors());
                        response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_CUSTOMMER_ERPNEXT, listCustomerDto, GlobalReponse.class);
                        if (response.getStatus().equals(HttpStatus.OK.value())) {
                            messageErpNext.append(response.getMessage());
                            STATUS = "COM";
                        } else
                            errorErpNext.append(response.getErrors());
                        listPartnerDto = new ArrayList<>();
                        listCustomerDto = new ArrayList<>();
                        uniquePartnerGroup = new HashSet<>();
                    }
                }
                interfaceIntegrationRepository.delete(itemP);
            }
            Optional<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIdAndIntStatus(value.getSyncIntegrationCredential().getIntegrationHistoryId(), "INP");
            if (integrationHistory.isPresent()) {
                IntegrationHistory history = integrationHistory.get();
                history.setIntStatus(STATUS);
                history.setDescription(errorErpNext.toString() + " \n " + messageErpNext.toString());
                this.integrationHistoryRepository.save(history);
            } else {
                log.info("not found integration history integration customer - vendor - partner group");
            }
        } else {
            try {
                for (InterfaceIntegration j : interfaceInt) {
                    JsonNode node = j.getData();
                    List<CustomerDto> customerDtos = new ArrayList<>();
                    List<VendorDto> vendorDtos = new ArrayList<>();
                    List<PartnerGroupDto> partnerGroupDtos = new ArrayList<>();
                    GlobalReponse response = new GlobalReponse();
                    // lay partner group
                    if (j.getType().equals(AppConstant.DataTypeInt.BPARTNER_GROUP)) {
                        for (JsonNode jPG : node) {
                            PartnerGroupDto group = PartnerGroupDto.builder()
                                    .groupName(jPG.path("name").asText())
                                    .isActive(jPG.path("isActive").asText())
                                    .groupCode(jPG.path("value").asText())
                                    .erpBpGroupId(jPG.path("C_BP_Group_ID").asInt())
                                    .isCustomer(jPG.path("isCustomer").asText())
                                    .build();
                            partnerGroupDtos.add(group);
                            if (!partnerGroupDtos.isEmpty()) {
                                response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_PARTNER_GROUP, partnerGroupDtos, GlobalReponse.class);
                                Status = response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI";
                            }
                        }
                    }
                    if (j.getType().equals(AppConstant.DataTypeInt.CUSTOMER)) {
                        for (JsonNode item : j.getData()) {
                            CustomerDto customerDto = CustomerDto.builder()
//                                        .name(item.path("userName").asText() + " " + item.path("partnerName").asText())
                                    .name(item.path("partnerName").asText())
                                    .discount(item.path("flatDiscount").decimalValue())
                                    .erpCustomerId(item.path("C_BPartner_ID").asInt())
                                    .isActive(item.path("isActive").asText())
                                    .code(item.path("value").asText())
                                    .partnerGroupId(item.path("C_BP_Group_ID").asInt())
                                    .phone1(item.path("phone").asText())
                                    .isPosVip(item.path("isPOSVIP").asText())
                                    .partnerName(item.path("partnerName").asText())
                                    .taxCode(item.path("taxId").asText())
                                    .email(item.path("email").asText())
                                    .creditLimit(item.path("creaditLimit").decimalValue())
                                    .debitAmount(item.path("debt").decimalValue())
                                    .gender(item.path("gender").asText())
                                    .build();
                            if (item.path("SOCreditStatus").asText().equals("O")) {
                                customerDto.setIsDebt("Y");
                            }
                            if (item.path("SOCreditStatus").asText().equals("X")) {
                                customerDto.setIsDebt("N");
                            }
                            if (item.get("Birthday") != null) {
                                customerDto.setBirthday(DateHelper.toLocalDate(item.path("Birthday").asText()));
                            }
                            if (item.path("address") != null) {
                                String address = item.path("address").asText().replace("+", " ");
                                customerDto.setAddress1(address);
                            }
                            customerDtos.add(customerDto);

                        }

                        if (!customerDtos.isEmpty()) {
                            response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_CUSTOMMER, customerDtos, GlobalReponse.class);
                            Status = response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI";
                        }

                    } else {
                        for (JsonNode item : j.getData()) {
                            VendorDto vendorDto = VendorDto.builder()
                                    .name(item.path("partnerName").asText())
                                    .isActive(item.path("isActive").asText())
                                    .code(item.path("value").asText())
                                    .partnerName(item.path("partnerName").asText())
                                    .description(item.path("description").asText())
                                    .isPosVip(item.path("isPOSVIP").asText())
                                    .code(item.path("value").asText())
                                    .disCount(item.path("flatDiscount").decimalValue())
                                    .phone1(item.path("phone").asText())
                                    .partnerGroupId(item.path("C_BP_Group_ID").asInt())
                                    .erpVendorId(item.path("C_BPartner_ID").asInt())
                                    .taxCode(item.path("taxId").asText())
                                    .email(item.path("email").asText())
                                    .creditLimit(item.path("creaditLimit").decimalValue())
                                    .debitAmount(item.path("debt").decimalValue())
                                    .build();
                            vendorDtos.add(vendorDto);
                        }
                        if (!vendorDtos.isEmpty()) {
                            response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_VENDOR, vendorDtos, GlobalReponse.class);
                            Status = response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI";
                        }
                    }
                    this.interfaceIntegrationRepository.delete(j);
                    kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_CAV_INT, value);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (interfaceInt.isEmpty()) {
                    IntegrationHistory intH = this.integrationHistoryRepository.findById(value.getSyncIntegrationCredential().getIntegrationHistoryId()).orElse(null);
                    if (intH != null) {
                        Status = AppConstant.IntStatus.COMPLETE;
                        intH.setIntStatus(Status);
                        this.integrationHistoryRepository.save(intH);
                    }
                }
            }
        }
        acknowledgment.acknowledge();
    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    private List<List<JsonNode>> splitList(JsonNode nodeList, int chunkSize) {
        List<List<JsonNode>> result = new ArrayList<>();
        List<JsonNode> temp = new ArrayList<>();

        for (int i = 0; i < nodeList.size(); i++) {
            temp.add(nodeList.get(i));
            if (temp.size() == chunkSize || i == nodeList.size() - 1) {
                result.add(new ArrayList<>(temp));
                temp.clear();
            }
        }
        return result;
    }

//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void sendKafkaAfterCommit(KafkaSendEvent event) {
//        kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_CAV_INT, event.getInterfaceIntegration());
//        log.info("🔥 Kafka message sent after transaction commit: " + event.getInterfaceIntegration());
//    }
//
//    // 4. Định nghĩa class Event
//    public static class KafkaSendEvent {
//        private final IntPartnerDto interfaceIntegration;
//
//        public KafkaSendEvent(Object source, IntPartnerDto interfaceIntegration) {
//            this.interfaceIntegration = interfaceIntegration;
//        }
//
//        public IntPartnerDto getInterfaceIntegration() {
//            return interfaceIntegration;
//        }
//    }
}
