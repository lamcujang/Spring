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
import com.dbiz.app.integrationservice.service.UserIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.tenantservice.service.data_source.DataSourceConfigService;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import com.dbiz.app.tenantservice.service.data_source.DataSourceRoutingService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.common.dbiz.dto.integrationDto.*;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.userDto.CustomerDto;
import org.common.dbiz.dto.userDto.IntPartnerDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPartnerCredential;
import org.common.dbiz.request.intergrationRequest.external.IntUserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserIntegrationServiceImpl implements UserIntegrationService {

    private final MessageSource messageSource;

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final ObjectMapper objectMapper;

    private final InterfaceIntegrationRepository interfaceIntegrationRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final IntegrationHistoryRepository integrationHistoryRepository;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    private final DataSourceContextHolder dataSourceContextHolder;

    private final DataSourceConfigService dataSourceConfigService;

    private final DataSourceRoutingService dataSourceRoutingService;

    /**
     *
     * @param credential
     * @return
     */
    @Override
    @Transactional
    public IntegrationHistory userIntegration(SyncIntegrationCredential credential) {
        log.info("**ServiceImpl::layer -  user integration **");
        StringBuilder error = new StringBuilder();
        GlobalReponse response = new GlobalReponse();
        IntegrationHistory integrationHistory = new IntegrationHistory();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        List<UserIntDto> getUserErp = new ArrayList<>();
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getUserErp = getUserFromIdempiere(credential, erpIntegration);
            if (credential.getIntType().equals("UPDATE") || (credential.getIntType().equals("INSERT")) && getUserErp.size() < 10) {
                response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_USER, getUserErp, GlobalReponse.class);
                if (response.getStatus() != HttpStatus.OK.value()) {
                    integrationHistory.setDescription(response.getMessage());
                } else {
                    error.append(response.getMessage());
                }
            }
        } else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getUserErp = getUserFromERPNext(credential, erpIntegration);
            if (credential.getSyncKafka().equals("N")) {
                response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_USER_ERPNEXT, getUserErp, GlobalReponse.class);
                if (response.getStatus() != HttpStatus.OK.value()) {
                    integrationHistory.setDescription(response.getMessage());
                } else {
                    error.append(response.getMessage());
                }
            }
        }
        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        if (credential.getIntType().equals("INSERT") && credential.getSyncKafka().equals("Y"))
            integrationHistory.setIntStatus("INP");
        else
            integrationHistory.setIntStatus(response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(getUserErp));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;

    }
    // 24/9/2024
    // lay user erp
    // chua dong bo ware house

    public List<UserIntDto> getUserFromIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp **");
        credential.setSyncKafka("N");
        List<UserIntDto> listUser = new ArrayList<>();
        boolean sendKafka = false;
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntUserCredential intUserCredential = IntUserCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .orgId(erpIntegration.getOrgId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.commonIntegrationService.getTokenIdempiere().getToken());


        try {
            HttpEntity<IntUserCredential> requestEntity = new HttpEntity<>(intUserCredential, headers);
            String url = erpIntegration.getErpUrl() + this.commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_USR);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {
                        List<UserOrgAccessIntDto> listUserOrg = new ArrayList<>();

                        // xu ly org access + posterminal
                        JsonNode orgAccess = node.path("org");
                        for (JsonNode item : orgAccess) {
                            List<PosTerminalOrgAccessIntDto> listPosTerminal = new ArrayList<>();

                            UserOrgAccessIntDto userOrgAccessDto = UserOrgAccessIntDto.builder()
                                    .orgId(item.path("AD_Org_Id").asInt())
                                    .isActive(item.path("isActive").asText())
                                    .build();

                            JsonNode itemPos = item.path("terminal");
                            for (JsonNode pos : itemPos) {
                                JsonNode infor = pos.path("infor");
                                PosTerminalDto posTerminalDto = PosTerminalDto.builder()
                                        .erpPosId(pos.path("C_POS_ID").asInt())
                                        .warehouseId(infor.path("M_Warehouse_ID").asInt())
                                        .isRestaurant(infor.path("isRestaurant").asText())
                                        .isNotifyBill(infor.path("isNotifyBill").asText())
                                        .priceListId(infor.path("M_PriceList_ID").asInt())
                                        .isActive(infor.path("isActive").asText())
                                        .bankAccountId(infor.path("C_BankAccount_ID").asInt())
                                        .bankAccountVisaId(infor.path("C_BankAccount_VISA_ID").asInt())
                                        .isBillMerge(infor.path("Bill_Mergeitems").asText())
                                        .orgId(pos.path("AD_Org_ID").asInt())
                                        .name(infor.path("Name").asText())
                                        .bankAccountCashId(infor.path("C_BankAccount_Cash_ID").asInt())
                                        .printerIp(infor.path("Printer_Host").asText())
                                        .printerPort(infor.path("Printer_Port").decimalValue())
                                        .build();

                                PosTerminalOrgAccessIntDto posTerminalOrgAccessDto = PosTerminalOrgAccessIntDto.builder()
                                        .posTerminalId(pos.path("C_POS_ID").asInt())
                                        .isActive(pos.path("isActive").asText())
                                        .infor(posTerminalDto)
                                        .build();
                                listPosTerminal.add(posTerminalOrgAccessDto);
                            }

                            JsonNode warehouse = item.path("warehouse");
                            List<UserOrgWarehouseAccessIntDto> listWarehouse = new ArrayList<>();
                            for (JsonNode wh : warehouse) {
                                UserOrgWarehouseAccessIntDto warehouseDto = UserOrgWarehouseAccessIntDto.builder()
                                        .warehouseId(wh.path("M_Warehouse_ID").asInt())
                                        .userId(wh.path("AD_User_ID").asInt())
                                        .isActive(wh.path("isActive").asText())
                                        .orgId(wh.path("AD_Org_ID").asInt())
                                        .build();
                                listWarehouse.add(warehouseDto);
                            }
                            userOrgAccessDto.setOrgWarehouseAccessIntDtos(listWarehouse);
                            userOrgAccessDto.setPosTerminalOrgAccessDtos(listPosTerminal);
                            listUserOrg.add(userOrgAccessDto);
                        }


                        // xu ly role
                        JsonNode roleAccess = node.path("roles");
                        List<UserRoleAccessIntDto> listRole = new ArrayList<>();
                        for (JsonNode role : roleAccess) {
                            UserRoleAccessIntDto userRoleAccessDto = UserRoleAccessIntDto.builder()
                                    .roleId(role.path("AD_Role_ID").asInt())
                                    .isActive(role.path("isActive").asText())
                                    .build();
                            listRole.add(userRoleAccessDto);
                        }


                        UserIntDto userDto = UserIntDto.builder()
                                .password(node.path("password").asText())
                                .erpUserId(node.path("AD_User_ID").asInt())
                                .userName(node.path("name").asText())
                                .fullName(node.path("name").asText().isEmpty() ? node.path("name").asText() : node.path("fullname").asText())
                                .email(node.path("email").asText())
                                .isActive(node.path("isActive").asText())
                                .phone(node.path("phone").asText())
                                .userOrgAccessDtos(listUserOrg)
                                .userRoleAccessDtos(listRole)
                                .build();
                        listUser.add(userDto);
                        if (listUser.size() == 10 && credential.getIntType().equals("INSERT")) {
//                            try {
//                                UserIntKafkaDto syncUser = UserIntKafkaDto.builder()
//                                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
//                                        .userIntDto(listUser)
//                                        .build();
//                                log.info("send message to kafka");
//                                syncUser.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
//                                syncUser.setLastPage("N");
//                                kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_CUS, AuditContext.getAuditInfo().getMainTenantId().toString(), syncUser);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                throw new PosException(messageSource.getMessage("error_during_syn", null, LocaleContextHolder.getLocale()));
//                            }
                            listUser = new ArrayList<>();
                            sendKafka = true;
                            InterfaceIntegration intFUser = InterfaceIntegration.builder()
                                    .data(root.path("data"))
                                    .type(AppConstant.DataTypeInt.USER)
                                    .status(AppConstant.IntStatus.NEW)
                                    .build();
                            this.interfaceIntegrationRepository.save(intFUser);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_during_syn", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }
        if (credential.getIntType().equals("INSERT") && sendKafka) {
            credential.setSyncKafka("Y");
            try {
                UserIntKafkaDto syncUser = UserIntKafkaDto.builder()
                        .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                        .userIntDto(listUser)
                        .build();
                log.info("send message to kafka");
                syncUser.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
                syncUser.setLastPage("Y");
                syncUser.setPlatForm(AppConstant.ERP_PLATFORM_IDEMPIERE);
                eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_INTERNAL_USER, syncUser));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listUser;
    }

    @Transactional
    public List<UserIntDto> getUserFromERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getUser from ERPNext **");
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
        String url = tokenERPNextRespDto.getUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_METHOD) + "/" + AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_USER;
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("intType", intUserCredential.getIntType());
        if (intUserCredential.getIntType().equals("UPDATE"))
            uri.queryParam("intDate", intUserCredential.getIntDate());
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<UserIntDto> listUser = new ArrayList<>();
        try {
            responseErpNext = externalRestTemplate.exchange(uri.build().toUriString(), HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("message").path("data");
                int size = root.path("message").path("size").intValue();
                if (size <= 20) {
                    for (JsonNode node : data) {
                        UserIntDto userIntDto = castJsonERPNext(node);
                        listUser.add(userIntDto);
                    }
                } else {
                    log.info("send kafka user");
                    credential.setSyncKafka("Y");
                    InterfaceIntegration interfaceIntegration = InterfaceIntegration.builder()
                            .data(data)
                            .type(AppConstant.DataTypeInt.USER)
                            .build();
                    interfaceIntegrationRepository.save(interfaceIntegration);
                    UserIntKafkaDto syncUser = UserIntKafkaDto.builder()
                            .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                            .userIntDto(listUser)
                            .build();
                    syncUser.setTenantId(AuditContext.getAuditInfo().getMainTenantId());
                    syncUser.setLastPage("N");
                    syncUser.setPlatForm(AppConstant.ERP_PLATFORM_ERPNEXT);
                    eventPublisher.publishEvent(new KafkaSendEvent(this, AppConstant.TopicKafka.TOPIC_INTERNAL_USER, syncUser));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(e.getMessage());
        }

        return listUser;
    }

    public UserIntDto castJsonERPNext(JsonNode jsonNode) {
        String url = this.commonIntegrationService.getTokenERPNext().getUrl();
        UserIntDto userCast = UserIntDto.builder()
                .userName(jsonNode.path("username").asText())
                .fullName(jsonNode.path("full_name").asText())
                .image(jsonNode.path("user_image").asText().isEmpty() ? null : ImageDto.builder().imageUrl(url + "/" + jsonNode.path("user_image").asText()).build())
                .email(jsonNode.path("email").asText(null))
                .phone(jsonNode.path("phone").asText(null))
                .isActive(jsonNode.path("disabled").equals("0") ? "Y" : "N")
                .erpUserName(jsonNode.path("name").asText()).build();
        return userCast;
    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_INTERNAL_USER, containerFactory = "kafkaListenerContainerFactory")
    public void receivedMessageCAV(ConsumerRecord<String, UserIntKafkaDto> consumerRecord, Acknowledgment acknowledgment) {
        log.info("kafla listener customer vendor");
        UserIntKafkaDto value = consumerRecord.value();
        String STATUS = "FAI";
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

        List<InterfaceIntegration> interfaceInt = interfaceIntegrationRepository.findByTypeAndStatus(AppConstant.DataTypeInt.USER, AppConstant.IntStatus.NEW);

        try {

            if (value.getPlatForm().equals(AppConstant.ERP_PLATFORM_ERPNEXT)) {
                StringBuilder messageErpNext = new StringBuilder("");
                StringBuilder errorErpNext = new StringBuilder("");
                List<UserIntDto> listUserInt = new ArrayList<>();
                for (InterfaceIntegration itemP : interfaceInt) {
                    for (JsonNode item : itemP.getData()) {
                        UserIntDto userInt = castJsonERPNext(item);
                        listUserInt.add(userInt);
                        if (listUserInt.size() == 20) {
                            GlobalReponse response = new GlobalReponse();
                            response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_USER_ERPNEXT, listUserInt, GlobalReponse.class);
                            if (response.getStatus().equals(HttpStatus.OK.value())) {
                                messageErpNext.append(response.getMessage());
                                STATUS = "COM";
                            } else
                                errorErpNext.append(response.getErrors());
                            listUserInt = new ArrayList<>();
                        }
                    }
                    interfaceIntegrationRepository.delete(itemP);
                }
            } else {
                Iterator<InterfaceIntegration> iterator = interfaceInt.iterator();
                while (iterator.hasNext()) {
                    InterfaceIntegration i = iterator.next();
                    List<UserIntDto> listUser = new ArrayList<>();
                    for (JsonNode node : i.getData()) {
                        List<UserOrgAccessIntDto> listUserOrg = new ArrayList<>();

                        // xu ly org access + posterminal
                        JsonNode orgAccess = node.path("org");
                        for (JsonNode item : orgAccess) {
                            List<PosTerminalOrgAccessIntDto> listPosTerminal = new ArrayList<>();

                            UserOrgAccessIntDto userOrgAccessDto = UserOrgAccessIntDto.builder()
                                    .orgId(item.path("AD_Org_Id").asInt())
                                    .isActive(item.path("isActive").asText())
                                    .build();

                            JsonNode itemPos = item.path("terminal");
                            for (JsonNode pos : itemPos) {
                                JsonNode infor = pos.path("infor");
                                PosTerminalDto posTerminalDto = PosTerminalDto.builder()
                                        .erpPosId(pos.path("C_POS_ID").asInt())
                                        .warehouseId(infor.path("M_Warehouse_ID").asInt())
                                        .isRestaurant(infor.path("isRestaurant").asText())
                                        .isNotifyBill(infor.path("isNotifyBill").asText())
                                        .priceListId(infor.path("M_PriceList_ID").asInt())
                                        .isActive(infor.path("isActive").asText())
                                        .bankAccountId(infor.path("C_BankAccount_ID").asInt())
                                        .bankAccountVisaId(infor.path("C_BankAccount_VISA_ID").asInt())
                                        .isBillMerge(infor.path("Bill_Mergeitems").asText())
                                        .orgId(pos.path("AD_Org_ID").asInt())
                                        .name(infor.path("Name").asText())
                                        .bankAccountCashId(infor.path("C_BankAccount_Cash_ID").asInt())
                                        .printerIp(infor.path("Printer_Host").asText())
                                        .printerPort(infor.path("Printer_Port").decimalValue())
                                        .build();

                                PosTerminalOrgAccessIntDto posTerminalOrgAccessDto = PosTerminalOrgAccessIntDto.builder()
                                        .posTerminalId(pos.path("C_POS_ID").asInt())
                                        .isActive(pos.path("isActive").asText())
                                        .infor(posTerminalDto)
                                        .build();
                                listPosTerminal.add(posTerminalOrgAccessDto);
                            }

                            JsonNode warehouse = item.path("warehouse");
                            List<UserOrgWarehouseAccessIntDto> listWarehouse = new ArrayList<>();
                            for (JsonNode wh : warehouse) {
                                UserOrgWarehouseAccessIntDto warehouseDto = UserOrgWarehouseAccessIntDto.builder()
                                        .warehouseId(wh.path("M_Warehouse_ID").asInt())
                                        .userId(wh.path("AD_User_ID").asInt())
                                        .isActive(wh.path("isActive").asText())
                                        .orgId(wh.path("AD_Org_ID").asInt())
                                        .build();
                                listWarehouse.add(warehouseDto);
                            }
                            userOrgAccessDto.setOrgWarehouseAccessIntDtos(listWarehouse);
                            userOrgAccessDto.setPosTerminalOrgAccessDtos(listPosTerminal);
                            listUserOrg.add(userOrgAccessDto);
                        }


                        // xu ly role
                        JsonNode roleAccess = node.path("roles");
                        List<UserRoleAccessIntDto> listRole = new ArrayList<>();
                        for (JsonNode role : roleAccess) {
                            UserRoleAccessIntDto userRoleAccessDto = UserRoleAccessIntDto.builder()
                                    .roleId(role.path("AD_Role_ID").asInt())
                                    .isActive(role.path("isActive").asText())
                                    .build();
                            listRole.add(userRoleAccessDto);
                        }


                        UserIntDto userDto = UserIntDto.builder()
                                .password(node.path("password").asText())
                                .erpUserId(node.path("AD_User_ID").asInt())
                                .userName(node.path("name").asText())
                                .fullName(node.path("name").asText().isEmpty() ? node.path("name").asText() : node.path("fullname").asText())
                                .email(node.path("email").asText())
                                .isActive(node.path("isActive").asText())
                                .phone(node.path("phone").asText())
                                .userOrgAccessDtos(listUserOrg)
                                .userRoleAccessDtos(listRole)
                                .build();
                        listUser.add(userDto);
                        if (listUser.size() > 10) {
                            GlobalReponse response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_USER, listUser, GlobalReponse.class);
                            listUser.clear();
                        }
                    }
                    if (!listUser.isEmpty()) {
                        GlobalReponse response = this.commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.USER_SERVICE_API_INTEGRATION_USER, listUser, GlobalReponse.class);
                        STATUS = response.getStatus().equals(HttpStatus.OK.value()) ? "COM" : "FAI";
                    }
                    this.interfaceIntegrationRepository.delete(i);
                    iterator.remove();
                    if (!iterator.hasNext()) ;
                    {
                        Optional<IntegrationHistory> integrationHistory = this.integrationHistoryRepository.findByIntTypeAndIntStatusAndTenantIdAndIsActive(AppConstant.DataTypeInt.USER, AppConstant.IntStatus.INPROCESSING, AuditContext.getAuditInfo().getTenantId(), "Y");
                        if (integrationHistory.isPresent()) {
                            IntegrationHistory history = integrationHistory.get();
                            history.setIntStatus("COM");
                            this.integrationHistoryRepository.save(history);
                        } else {
                            log.info("not found integration history integration user");
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }

    }

    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    public PosTerminalDto castJsonToDtoIdempiere(JsonNode jsonNode) {
        return null;
    }
}
