package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.ErpIntegrationService;
import com.dbiz.app.integrationservice.service.IntegrationHistoryService;
import com.dbiz.app.integrationservice.service.VoucherIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.config.DObjectMapper;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.integrationDto.voucher.CheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntCheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntGetVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.CheckInVoucherDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.IntCheckInVoucherDetailDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.IntCheckInVoucherDto;
import org.common.dbiz.dto.orderDto.PosOrderDto;
import org.common.dbiz.dto.paymentDto.VoucherInfo;
import org.common.dbiz.dto.paymentDto.VoucherTransactionDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.external.GetTokenCredential;
import org.common.dbiz.request.intergrationRequest.external.IntOrgCredential;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class VoucherIntegrationServiceImpl implements VoucherIntegrationService {

    private final ErpIntegrationRepository erpIntegrationRepository;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    private final MessageSource messageSource;

    private final EntityManager entityManager;

    private final CommonIntegrationService commonService;
    private final ObjectMapper objectMapper;
    private final IntegrationHistoryService integrationHistoryService;


    @Override
    public GlobalReponse checkVoucherInfo(CheckVoucherInfoDto dto) {

        log.info("** checkVoucherInfo **");
        GlobalReponse response = new GlobalReponse();
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        IntCheckVoucherInfoDto intCheckVoucherInfoDto = IntCheckVoucherInfoDto.builder()
                .ad_User_ID(dto.getErpUserId())
                .voucherCode(dto.getVoucherCode())
                .serviceId(dto.getServiceId())
                .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;
        String payload = "";
        try{
            payload = objectMapper.writeValueAsString(intCheckVoucherInfoDto);
            HttpEntity<IntCheckVoucherInfoDto> requestEntity = new HttpEntity<>(intCheckVoucherInfoDto, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_CHECK_VOUCHER_INFO);
            log.info("url: " + url);
            log.info("payload: " + payload);
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                if (jsonNode.get("success").asBoolean() == true) {
                    response.setStatus(HttpStatus.OK.value());
                    response.setData(jsonNode.get("data"));
                }else{
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                response.setMessage(jsonNode.get("messages").asText());

                IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                        .intType(AppConstant.ErpIntegrationType.VOUCHER)
                        .orgId(dto.getOrgId())
                        .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                        .intStatus(
                                response.getStatus().intValue() == HttpStatus.OK.value()
                                        ?  AppConstant.ErpIntegrationStatus.COMPLETE
                                        : AppConstant.ErpIntegrationStatus.FAIL)
                        .userId(dto.getUserId())
                        .payload(payload)
                        .response(jsonNode.toString())
                        .build();
                integrationHistoryService.saveIntegrationHistory(integrationHistory);

                return response;
            }else{
                throw new PosException(messageSource.getMessage("erp.integration.failed", null, LocaleContextHolder.getLocale()));
            }
        }catch (Exception e){
            IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                    .intType(AppConstant.ErpIntegrationType.VOUCHER)
                    .orgId(dto.getOrgId())
                    .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                    .intStatus(AppConstant.ErpIntegrationStatus.FAIL)
                    .userId(dto.getUserId())
                    .payload(payload)
                    .response(responseIdempiere != null ? responseIdempiere.getBody() : null)
                    .build();
            integrationHistoryService.saveIntegrationHistory(integrationHistory);
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

    }

    @Override
    public GlobalReponse getVoucherServices(VoucherParamDto dto) {

        log.info("** getVoucherServices **");

        GlobalReponse response = new GlobalReponse();
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        ResponseEntity<String> responseIdempiere = null;
        String payload = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenIdempiereRespDto.getToken());
            HttpEntity<IntCheckVoucherInfoDto> requestEntity = new HttpEntity<>(headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_GET_VOUCHER_SERVICES);
            url += "?$orderby=sequence_no&" +
                    "$filter=ad_client_id eq "
                    + tokenIdempiereRespDto.getAdClientId()
                    + " and  lookup_type_code eq 'CUS_LOAIDICHVU'"
                    +"&$top="+dto.getPageSize()+"&$skip=" + dto.getPage();
            log.info("url: " + url);
            payload = url;
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                response.setStatus(HttpStatus.OK.value());
                response.setData(jsonNode.get("records"));
                response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));

                IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                        .intType(AppConstant.ErpIntegrationType.VOUCHER)
                        .orgId(dto.getOrgId())
                        .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                        .intStatus( AppConstant.ErpIntegrationStatus.COMPLETE)
                        .userId(dto.getUserId())
                        .payload(payload)
                        .response(jsonNode.toString())
                        .build();
                integrationHistoryService.saveIntegrationHistory(integrationHistory);

                return response;
            }else{
                throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
            }

        }catch (Exception e){
            IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                    .intType(AppConstant.ErpIntegrationType.VOUCHER)
                    .orgId(dto.getOrgId())
                    .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                    .intStatus(AppConstant.ErpIntegrationStatus.FAIL)
                    .userId(dto.getUserId())
                    .payload(payload)
                    .response(responseIdempiere != null ? responseIdempiere.getBody() : null)
                    .build();
            integrationHistoryService.saveIntegrationHistory(integrationHistory);
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

    }

    @Override
    public GlobalReponsePagination getVoucherServiceOrders(VoucherParamDto dto) {

        log.info("** getVoucherServiceOrder **");

        GlobalReponsePagination response = new GlobalReponsePagination();

        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();



        ResponseEntity<String> responseIdempiere = null;
        String payload = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(tokenIdempiereRespDto.getToken());
            HttpEntity<IntCheckVoucherInfoDto> requestEntity = new HttpEntity<>(headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_GET_VOUCHER_SERVICE_ORDER);
            url +=
                    "?$filter=ad_client_id eq "
                    + tokenIdempiereRespDto.getAdClientId();

            if(dto.getDocumentNo() != null){
                url += " and contains(documentno,'" + dto.getDocumentNo() + "') ";
            }

            url +=
                    "&$top="+dto.getPageSize()+"&$skip=" + dto.getPage();
            log.info("url: " + url);
            payload = url;
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                response.setStatus(HttpStatus.OK.value());
                response.setTotalPages(jsonNode.get("page-count").asInt());
                response.setCurrentPage(jsonNode.get("skip-records").asInt());
                response.setPageSize(jsonNode.get("row-count").asInt());
                response.setTotalItems(jsonNode.get("records-size").asLong());
                response.setData(jsonNode.get("records"));
                response.setMessage(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()));

                IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                        .intType(AppConstant.ErpIntegrationType.VOUCHER)
                        .orgId(dto.getOrgId())
                        .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                        .intStatus( AppConstant.ErpIntegrationStatus.COMPLETE)
                        .userId(dto.getUserId())
                        .payload(payload)
                        .response(jsonNode.toString())
                        .build();
                integrationHistoryService.saveIntegrationHistory(integrationHistory);

                return response;
            }else{
                throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
            }

        }catch (Exception e){
            IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                    .intType(AppConstant.ErpIntegrationType.VOUCHER)
                    .orgId(dto.getOrgId())
                    .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                    .intStatus(AppConstant.ErpIntegrationStatus.FAIL)
                    .userId(dto.getUserId())
                    .payload(payload)
                    .response(responseIdempiere != null ? responseIdempiere.getBody() : null)
                    .build();
            integrationHistoryService.saveIntegrationHistory(integrationHistory);
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

    }

    @Override
    public GlobalReponse getVoucherInfo(CheckVoucherInfoDto dto) {

        log.info("** getVoucherInfo **");
        GlobalReponse response = new GlobalReponse();
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        IntGetVoucherInfoDto intCheckVoucherInfoDto = IntGetVoucherInfoDto.builder()
                .voucherNo(dto.getVoucherCode())
                .build();

        String sql = "SELECT sum(amount) as amt,COALESCE(MAX(owner_name), null) AS name " +
                " FROM pos.d_voucher_transaction WHERE d_tenant_id = :tenantId " +
                " AND voucher_code = :voucherCode " ;



        // Sử dụng native query và AliasToEntityMapResultTransformer để ánh xạ kết quả thành Map
        List<Map<String, Object>> results = entityManager.createNativeQuery(sql)
                .setParameter("tenantId", AuditContext.getAuditInfo().getTenantId())
                .setParameter("voucherCode", dto.getVoucherCode())
                .unwrap(NativeQuery.class)
                .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();

        boolean check = true;
        if (results.size() > 0){
            VoucherInfo voucherInfo = new VoucherInfo();
            for (Map<String, Object> row : results) {
                String ownerName = ParseHelper.STRING.parse(row.get("name"));
                BigDecimal balanceAmt = ParseHelper.BIGDECIMAL.parse(row.get("amt"));
                log.info("ownerName: " + ownerName);
                log.info("balanceAmt: " + balanceAmt);
                if(balanceAmt == null && ownerName == null) {
                    check = false;
                    break;
                }
                voucherInfo.setCustomerName(ownerName);
                voucherInfo.setBalanceAmt(balanceAmt);
                voucherInfo.setVoucherCode(dto.getVoucherCode());
            }

            log.info("check: " + check);
            if (check){
                boolean isValid = voucherInfo.getBalanceAmt() != null
                        && voucherInfo.getBalanceAmt().compareTo(BigDecimal.ZERO) > 0;
                response.setStatus(isValid ? HttpStatus.OK.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setData( isValid ? voucherInfo : null);
                response.setMessage( isValid
                        ?  messageSource.getMessage("successfully",
                        null, LocaleContextHolder.getLocale())
                        :  dto.getVoucherCode() + " " + messageSource.getMessage("voucher.invalid", null,
                        LocaleContextHolder.getLocale()));
                return response;
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;
        String payload = "";
        try{
            payload = objectMapper.writeValueAsString(intCheckVoucherInfoDto);
            HttpEntity<IntGetVoucherInfoDto> requestEntity = new HttpEntity<>(intCheckVoucherInfoDto, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_GET_VOUCHER_INFO);
            log.info("url: " + url);
            log.info("payload: " + payload);
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                BigDecimal amt = null;
                if (jsonNode.get("success").asBoolean() == true) {
                    response.setStatus(HttpStatus.OK.value());
                    response.setData(jsonNode.get("data"));
                    amt = new BigDecimal(jsonNode.get("data").get("balanceAmt").asText());
                    // Save voucher transaction
                    saveVoucherTransaction(jsonNode.get("data"),
                            dto.getOrgId(),
                            dto.getVoucherCode());
                }else{
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }

                boolean isInvalid = amt != null && amt.compareTo(BigDecimal.ZERO) <= 0;

                if(isInvalid) response.setData(null);
                
                response.setMessage(isInvalid
                        ? dto.getVoucherCode() + " " + messageSource.getMessage("voucher.invalid", null,
                        LocaleContextHolder.getLocale())
                        : jsonNode.get("messages").asText());

                IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                        .intType(AppConstant.ErpIntegrationType.VOUCHER)
                        .orgId(dto.getOrgId())
                        .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                        .intStatus(
                                response.getStatus().intValue() == HttpStatus.OK.value()
                                        ?  AppConstant.ErpIntegrationStatus.COMPLETE
                                        : AppConstant.ErpIntegrationStatus.FAIL)
                        .userId(dto.getUserId())
                        .payload(payload)
                        .response(jsonNode.toString())
                        .build();
                integrationHistoryService.saveIntegrationHistory(integrationHistory);

                return response;
            }else{
                throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
            }
        }catch (Exception e){
            IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                    .intType(AppConstant.ErpIntegrationType.VOUCHER)
                    .orgId(dto.getOrgId())
                    .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                    .intStatus(AppConstant.ErpIntegrationStatus.FAIL)
                    .userId(dto.getUserId())
                    .payload(payload)
                    .response(responseIdempiere != null ? responseIdempiere.getBody() : null)
                    .build();
            integrationHistoryService.saveIntegrationHistory(integrationHistory);
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }

    }

    @Override
    public GlobalReponse checkInVoucher(CheckInVoucherDto dto) {

        log.info("** checkInVoucherInfo **");
        GlobalReponse response = new GlobalReponse();
        TokenIdempiereRespDto tokenIdempiereRespDto = commonService.getTokenIdempiere();

        IntCheckInVoucherDto intCheckInVoucherDto = IntCheckInVoucherDto.builder()
                .ad_Client_ID(tokenIdempiereRespDto.getAdClientId())
                .c_BPartner_ID(dto.getErpCustomerId())
                .c_Order_ID(dto.getErpOrderId())
                .ad_User_ID(dto.getErpUserId())
                .phoneNumber(dto.getPhoneNumber())
                .registerNo(dto.getRegisterNo())
                .m_Warehouse_ID(dto.getErpWarehouseId())
                .terminalID(dto.getErpPosTerminalId())
                .data(dto.getData().stream()
                        .map(item -> IntCheckInVoucherDetailDto.builder()
                                .c_OrderLine_ID(item.getErpOrderLineId())
                                .voucherCode(item.getVoucherCode())
                                .statusCode(item.getStatusCode())
                                .voucher_id(item.getVoucherId())
                                .service_id(item.getServiceId())
                                .dbiz_product_service_id(item.getProductServiceId())
                                .build())
                        .collect(Collectors.toList())
                )
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());
        ResponseEntity<String> responseIdempiere = null;
        String payload = "";
        try{
            payload = objectMapper.writeValueAsString(intCheckInVoucherDto);
            HttpEntity<IntCheckInVoucherDto> requestEntity = new HttpEntity<>(intCheckInVoucherDto, headers);
            String url = tokenIdempiereRespDto.getUrl() + commonService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_CHECKIN_VOUCHER);
            log.info("url: " + url);
            responseIdempiere = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (responseIdempiere.getStatusCode().is2xxSuccessful()){
                String responseBody = responseIdempiere.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                if (jsonNode.get("success").asBoolean() == true) {
                    response.setStatus(HttpStatus.OK.value());
                    response.setData(jsonNode.get("data"));
                }else{
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                response.setMessage(jsonNode.get("messages").asText());

                IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                        .intType(AppConstant.ErpIntegrationType.VOUCHER)
                        .orgId(dto.getOrgId())
                        .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                        .intStatus(
                                response.getStatus().intValue() == HttpStatus.OK.value()
                                        ?  AppConstant.ErpIntegrationStatus.COMPLETE
                                        : AppConstant.ErpIntegrationStatus.FAIL)
                        .userId(dto.getUserId())
                        .payload(payload)
                        .response(jsonNode.toString())
                        .build();
                integrationHistoryService.saveIntegrationHistory(integrationHistory);

                return response;
            }else{
                throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
            }
        }catch (Exception e){
            IntegrationHistoryDto integrationHistory = IntegrationHistoryDto.builder()
                    .intType(AppConstant.ErpIntegrationType.VOUCHER)
                    .orgId(dto.getOrgId())
                    .intFlow(AppConstant.ErpIntegrationFlow.FROM_ERP)
                    .intStatus(AppConstant.ErpIntegrationStatus.FAIL)
                    .userId(dto.getUserId())
                    .payload(payload)
                    .response(responseIdempiere != null ? responseIdempiere.getBody() : null)
                    .build();
            integrationHistoryService.saveIntegrationHistory(integrationHistory);
            throw new PosException(messageSource.getMessage("failed", null, LocaleContextHolder.getLocale()));
        }


        }

    public String saveVoucherTransaction(JsonNode js,Integer orgId,String voucherCode){

        try {
            VoucherTransactionDto dto = VoucherTransactionDto.builder()
                    .orgId(orgId)
                    .ownerName(js.get("customerName").asText())
                    .voucherCode(voucherCode)
                    .amount(new BigDecimal(js.get("balanceAmt").asText())).build();
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<VoucherTransactionDto> requestEntity = new HttpEntity<>(dto, headers);

            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_SAVE_VOUCHER_TRANSACTION, requestEntity, GlobalReponse.class);

            if (response.getStatus() != HttpStatus.OK.value()) {
                return "FAI";
            }
            return "COM";
        }catch (Exception e) {
            throw new PosException("Failed to save voucher transaction");
        }
    }
}
