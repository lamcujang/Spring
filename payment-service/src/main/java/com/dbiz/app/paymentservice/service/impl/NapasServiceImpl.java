package com.dbiz.app.paymentservice.service.impl;


import com.dbiz.app.paymentservice.constant.AppConstant;
import com.dbiz.app.paymentservice.domain.NapasReconciliation;
import com.dbiz.app.paymentservice.domain.NapasReconciliationDT;
import com.dbiz.app.paymentservice.domain.NapasTransaction;
import com.dbiz.app.paymentservice.repository.NapasReconciliationDTRepository;
import com.dbiz.app.paymentservice.service.NapasConfigService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.service.TenantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.common.dbiz.dto.IdentityDto.User.IDUserDto;
import org.common.dbiz.dto.paymentDto.BankIntegrationInfoDto;
import org.common.dbiz.dto.paymentDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.napas.*;
import org.common.dbiz.dto.paymentDto.request.QRCodeReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigReqDto;
import org.common.dbiz.dto.systemDto.NapasConfigResDto;
import org.common.dbiz.exception.wrapper.NapasErrorException;
import com.dbiz.app.paymentservice.repository.NapasReconciliationRepository;
import com.dbiz.app.paymentservice.repository.NapasTransactionRepository;
import com.dbiz.app.paymentservice.service.NapasService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.exception.wrapper.NapasSignatureInvalidException;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class NapasServiceImpl implements NapasService {

    private final NapasTransactionRepository napasTransactionRepository;
    private final NapasReconciliationRepository napasReconciliationRepository;
    private final NapasReconciliationDTRepository napasReconciliationDTRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final TenantService tenantService;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;
    private final NapasConfigService napasConfigService;


    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;



    @Override
    public GlobalReponse createNapasTransaction(NotificationDto dto) {

        log.info("createNapasTransaction");

        NapasTransaction napasTransaction = null;
        Integer transactionId = null;
        try{

            log.info("payload: " + objectMapper.writeValueAsString(dto));
            napasTransaction = modelMapper.map(dto, NapasTransaction.class);


            napasTransaction.setTenantId(AuditContext.getAuditInfo().getTenantId());
            napasTransaction.setOrgId(getOrgByBranchCode(dto.getId()));
            napasTransaction.setTrxId(dto.getId());
            napasTransaction.setCreationDateTime(OffsetDateTime.parse(dto.getCreationDateTime()));
            napasTransaction.setTransDateTime(OffsetDateTime.parse(dto.getTransDateTime()));
            napasTransaction  = napasTransactionRepository.saveAndFlush(napasTransaction);

        }catch (Exception e){
            log.error("Error while creating Napas transaction: {}", e.getMessage());
            throw new NapasErrorException("Failed to create Napas transaction");
        }

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, null))
                .status(HttpStatus.OK.value())
                .data(napasTransaction.getNpTransactionId())
                .build();
    }


    @Override
    public NapasResponseDto createNapasReconciliation(PayloadNapasDto req) {

        log.info("createNapasReconciliation");
        ReconciliationReportDto dto = null;
        NapasReconciliation napasReconciliation = null;
        try {
            dto = objectMapper.convertValue(req.getPayload(), ReconciliationReportDto.class);
            log.info("payload: " + objectMapper.writeValueAsString(req));
        }catch (Exception e){
            log.error("Error while creating Napas reconciliation: {}", e.getMessage());
            throw new NapasErrorException("Failed to create Napas reconciliation");
        }
        verifySignature(req);

        try{
            String id = null;
            try {

                if(dto.getTranxDetail() == null || dto.getTranxDetail().isEmpty()){
                    throw new NapasErrorException("Reconciliation Detail is null or empty");
                }
                id = dto.getTranxDetail().get(0).getId();
                changeTenant(id);
            } catch (PosException e) {
                log.error("Error while changing tenant: {}", e.getMessage());
                throw new NapasErrorException("Invalid Reconciliation ID");
            }
            Integer orgId = getOrgByBranchCode(id);
            napasReconciliation = modelMapper.map(dto, NapasReconciliation.class);
            napasReconciliation.setTenantId(AuditContext.getAuditInfo().getTenantId());
            napasReconciliation.setOrgId(orgId);
            napasReconciliation.setCreationDateTime(OffsetDateTime.parse(dto.getCreationDateTime()));
            napasReconciliation.setBeginDate(OffsetDateTime.parse(dto.getBeginDate()));
            napasReconciliation.setEndDate(OffsetDateTime.parse(dto.getEndDate()));

            napasReconciliation = napasReconciliationRepository.saveAndFlush(napasReconciliation);

            processReconciliationDTList(orgId, napasReconciliation.getNpReconciliationId(), dto.getTranxDetail());
        }catch (Exception e){
            log.error("Error while creating Napas reconciliation: {}", e.getMessage());
            throw new NapasErrorException("Failed to create Napas reconciliation");
        }

        return NapasResponseDto.builder()
                .code("success")
                .message("Message is successfully")
                .build();
    }


    public void processReconciliationDTList(Integer orgId, Integer reconciliationId,
                                              List<ReconciliationReportDTDto> tranxDetail) {


        try {
            NapasReconciliationDT napasReconciliationDT = null;
            StringBuilder sb = new StringBuilder();
            for (ReconciliationReportDTDto detail : tranxDetail) {
                napasReconciliationDT = modelMapper.map(detail, NapasReconciliationDT.class);
                napasReconciliationDT.setTenantId(AuditContext.getAuditInfo().getTenantId());
                napasReconciliationDT.setOrgId(orgId);
                napasReconciliationDT.setNpReconciliationId(reconciliationId);
                napasReconciliationDT.setTransDateTime(OffsetDateTime.parse(detail.getTransDateTime()));
                napasReconciliationDT.setTrxId(detail.getId());
                napasReconciliationDTRepository.saveAndFlush(napasReconciliationDT);
            }
        }catch (Exception e){
            log.error("Error while creating Napas reconciliation detail: {}", e.getMessage());
            throw new NapasErrorException("Failed to create Napas reconciliation detail");
        }

    }

    @Override
    public GlobalReponse checkNapasQRCode(CheckNapasQRCodeDto dto) {

        log.info("checkNapasQRCode: {}", dto);

        String token = getNapasToken();
        if (token == null) {
            throw new PosException(messageSource.getMessage("napas.check.qrcode.fail", null, LocaleContextHolder.getLocale()));
        }

        String response = processNapasInvestigation(token,dto);
        if (response.equals("FAI")) {
            throw new PosException(messageSource.getMessage("napas.check.qrcode.fail", null, LocaleContextHolder.getLocale()));
        }

        return GlobalReponse.builder()
                .message(messageSource.getMessage("successfully", null, null))
                .status(HttpStatus.OK.value())
                .data(token)
                .build();
    }


    public String getNapasToken(){

        log.info("getNapasToken");
        NapasConfigResDto napasConfigResDto = napasConfigService.getNapasConfig("TOKEN");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

//        GetNapasTokenDto tokenDto = GetNapasTokenDto.builder()
//                .grantType(napasConfigResDto.getGrantType())
//                .clientId(napasConfigResDto.getClientId())
//                .clientSecret(napasConfigResDto.getClientSecret())
//                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", napasConfigResDto.getGrantType());
        formData.add("client_id", napasConfigResDto.getClientId());
        formData.add("client_secret", napasConfigResDto.getClientSecret());


        try{
            log.info("payload: " + objectMapper.writeValueAsString(formData));
            ResponseEntity<String> rsp = null;
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
            log.info("url: " + napasConfigResDto.getUrl());
            rsp = externalRestTemplate.exchange(napasConfigResDto.getUrl(), HttpMethod.POST, requestEntity, String.class);
            if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                GetNapasTokenResDto dto = objectMapper.readValue(rsp.getBody(),GetNapasTokenResDto.class);
                return dto.getAccessToken();
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }


    public String processNapasInvestigation(String token,CheckNapasQRCodeDto dto)  {

        log.info("processNapasInvestigation");



        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        //Napas Config Info
        NapasConfigResDto napasConfigResDto = napasConfigService.getNapasConfig("INVESTIGATION");

        //Payload to send Napas
        PayloadNapasDto payloadNapasDto = new PayloadNapasDto();

        //Body to send Napas
        InvestigationDto investigationDto = getInvestigationPayload(dto, napasConfigResDto);
        payloadNapasDto.setPayload(investigationDto);
        HeaderPayloadNapasDto headerDto = getNapasHeader(payloadNapasDto);
        //Set case ID
        investigationDto.setCaseId(headerDto.getSenderReference());
        payloadNapasDto.setPayload(investigationDto);
        payloadNapasDto.setHeader(headerDto);

        log.info("UUID length: " + UUID.randomUUID().toString().length());
        ResponseEntity<String> rsp = null;
        try{
            log.info("body: " + objectMapper.writeValueAsString(investigationDto));
            log.info("payload: " + objectMapper.writeValueAsString(payloadNapasDto));


            if(dto.getSitTest() != null && dto.getSitTest().equals("TEST")){
                HttpEntity<PaymentDto> requestEntity = new HttpEntity<>(PaymentDto.builder().build(), headers);
                log.info("url: " + napasConfigResDto.getUrl());
                log.info("entity: "  + requestEntity.toString());
                rsp = externalRestTemplate.exchange(napasConfigResDto.getUrl(), HttpMethod.POST, requestEntity, String.class);
                if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                    return "COM";
                }else{
                    return "FAI";
                }
            }else{
                HttpEntity<PayloadNapasDto> requestEntity = new HttpEntity<>(payloadNapasDto, headers);
                log.info("url: " + napasConfigResDto.getUrl());
                log.info("entity: "  + requestEntity.toString());
                rsp = externalRestTemplate.exchange(napasConfigResDto.getUrl(), HttpMethod.POST, requestEntity, String.class);
                if (rsp.getStatusCode().value() == HttpStatus.OK.value()){
                    return "COM";
                }else{
                    return "FAI";
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return "FAI";

    }


    public void changeTenant(String code){
        String newCode = code.substring(4,7);
        tenantService.changeTenantByMerchantCode(newCode);
    }


    public Integer getOrgByBranchCode(String merchantCode) {

        merchantCode = merchantCode.substring(7, 10);

        String sql1 = "select d_org_id from pos.d_org where 1 = 1 and np_branch_code = :code";

        List<?> resultList = entityManager.createNativeQuery(sql1)
                .setParameter("code", merchantCode)
                .getResultList();

        if (resultList.isEmpty()) {
            throw new NapasErrorException("Branch not found");
        }

        return  ((Number) resultList.get(0)).intValue();

    }

    public InvestigationDto getInvestigationPayload(CheckNapasQRCodeDto dto,
                                                   NapasConfigResDto napasConfigResDto) {


        try {

            String sql = "select dpo.d_pos_order_id,dpo.d_np_order_id, dpp.payment_method, " +
                    "           dpp.total_amount,dpp.created  " +
                    "     from d_pos_order dpo  " +
                    "     inner join d_pos_payment dpp on dpo.d_pos_order_id = dpp.d_pos_order_id " +
                    "     where dpo.d_pos_order_id = :orderId and dpp.payment_method = :paymentMethod ";

            List<Map<String, Object>> results = null;
            // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
            results = entityManager.createNativeQuery(sql)
                    .setParameter("orderId", dto.getPosOrderId())
                    .setParameter("paymentMethod", AppConstant.PaymentRule.QRCODE_NAPAS)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                    .getResultList();


            String napasType = napasConfigResDto.getNapasCode();
            String masterMerchant = napasConfigResDto.getMasterMerchantCode();

            //Bank Integration Info
            BankIntegrationInfoDto bankIntegrationInfo = napasConfigService.getBankIntegrationInfo(dto.getOrgId(), dto.getPosTerminalId());
            String identifyCode = napasType
                    + masterMerchant
                    + bankIntegrationInfo.getMerchantCode()
                    + bankIntegrationInfo.getBranchCode()
                    + bankIntegrationInfo.getPosCode();

            Integer npOrderId = 0;
            BigDecimal amount = BigDecimal.ZERO;
            String paymentDate = "";



            for (Map<String, Object> row : results) {
                log.info("Row: {}", row);

                npOrderId = ParseHelper.INT.parse(row.get("d_np_order_id"));
                amount = ParseHelper.BIGDECIMAL.parse(row.get("total_amount"));
                paymentDate = DateHelper.fromInstantUTC(ParseHelper.INSTANT.parse(row.get("created")));

            }
            identifyCode  = identifyCode + npOrderId;
            paymentDate = DateHelper.convertTimeNZtoTimeZ(paymentDate);
            InvestigationDto investigationDto = InvestigationDto.builder()
                    .caseId(DateHelper.generateTimeId())
                    .id(identifyCode)
                    .transDateTime(paymentDate)
                    .amount(amount)
                    .build();

            return investigationDto;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new PosException(e.getMessage());
        }
    }


    public HeaderPayloadNapasDto getNapasHeader(PayloadNapasDto dto) {

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        // Add headers as needed
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());


        // Create an HttpEntity with the headers and the request body
        HttpEntity<PayloadNapasDto> requestEntity = new HttpEntity<>(dto, headers);
        //Response from Invoice Service
        GlobalReponse response = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_GET_NAPAS_HEADER ,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();

        if (response == null || response.getStatus() != HttpStatus.OK.value()) {
            throw new PosException("Failed to get Napas Header");
        } else {
            return  modelMapper.map(response.getData(), HeaderPayloadNapasDto.class );
        }
    }


    public void verifySignature(PayloadNapasDto dto) {

        log.info("verifySignature");
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        // Add headers as needed
//        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());


        // Create an HttpEntity with the headers and the request body
        HttpEntity<PayloadNapasDto> requestEntity = new HttpEntity<>(dto, headers);
        //Response from Invoice Service
        GlobalReponse response = this.restTemplate
                .postForEntity(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_VERIFY_NAPAS_SIGNATURE ,
                        requestEntity,
                        GlobalReponse.class)
                .getBody();

        if(response == null || response.getStatus() == HttpStatus.MULTIPLE_CHOICES.value()) {
            throw new NapasSignatureInvalidException(response.getMessage());
        }
    }

}
