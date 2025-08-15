package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.PosTerminalIntService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.productDto.PriceListIntDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.PosTerminalIntDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPartnerCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPosTerminalCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PosTerminalIntServiceImpl implements PosTerminalIntService {
    private final MessageSource messageSource;

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final ObjectMapper objectMapper;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public IntegrationHistory posTerminalIntegration(SyncIntegrationCredential credential) {
//        getPosTerminalErp
       StringBuilder message = new StringBuilder();
        IntegrationHistory integrationHistory = new IntegrationHistory();
        log.info("**ServiceImpl::layer -  posTerminal integration **");
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        List<PosTerminalDto> getPosTerminal = null;
        PosTerminalIntDto posTerminalIntDto = new PosTerminalIntDto();
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getPosTerminal = getPosTerminalIdempiere(credential, erpIntegration);
            posTerminalIntDto.setType(AppConstant.ERP_PLATFORM_IDEMPIERE);
            posTerminalIntDto.setListPosterminalDto(getPosTerminal);
        }
        else if(erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS))
        {
            getPosTerminal = getPosTerminalERPNext(credential, erpIntegration);
            posTerminalIntDto.setType(AppConstant.ERP_PLATFORM_ERPNEXT);
            posTerminalIntDto.setListPosterminalDto(getPosTerminal);
        }
        GlobalReponse response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_INTEGRATION_POSTEMINAL, posTerminalIntDto, GlobalReponse.class);
        if (response.getStatus() != HttpStatus.OK.value()) {
            message.append(response.getErrors());
        }
        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(message.toString());
        integrationHistory.setPayload(Json.pretty(getPosTerminal));
        integrationHistory.setResponse(Json.pretty(response));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;
    }


    // get pos terminal from erp

    public List<PosTerminalDto> getPosTerminalIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp **");
        List<PosTerminalDto> listPos = new ArrayList<>();
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntPosTerminalCredential intPosCredential = IntPosTerminalCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .orgId(erpIntegration.getOrgId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(this.commonIntegrationService.getTokenIdempiere().getToken());


        try {
            HttpEntity<IntPosTerminalCredential> requestEntity = new HttpEntity<>(intPosCredential, headers);
            String url = erpIntegration.getErpUrl() + this.commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_POS);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {
                        PosTerminalDto posTerminalDto = PosTerminalDto.builder()
                                .erpPosId(node.path("C_POS_ID").asInt())
                                .warehouseId(node.path("M_Warehouse_ID").asInt())
                                .isRestaurant(node.path("isRestaurant").asText())
                                .isNotifyBill(node.path("isNotifyBill").asText())
                                //sales rep id
                                .priceListId(node.path("M_PriceList_ID").asInt())
                                .isActive(node.path("isActive").asText())
                                .bankAccountId(node.path("C_BankAccount_ID").asInt())
                                .bankAccountVisaId(node.path("C_BankAccount_VISA_ID").asInt())
                                .isBillMerge(node.path("Bill_Mergeitems").asText())
                                .isNotifyBill(node.path("IsNotifyBill").asText())
                                .orgId(node.path("AD_Org_ID").asInt())
                                .name(node.path("name").asText())
                                .bankAccountCashId(node.path("C_BankAccount_Cash_ID").asInt())
                                .printerIp(node.path("Printer_Host").asText())
                                .printerPort(BigDecimal.valueOf(Long.parseLong(node.path("Printer_Port").asText())))
                                .userId(node.path("SalesRep_ID").asInt())
                                .build();

                        // infro pricelist
                        PriceListIntDto priceInt = PriceListIntDto.builder()
                                .pricePrecision(node.get("pricePrecision").asInt())
                                .isDefault(node.get("isDefault") != null ? node.get("isDefault").asText() : "N")
                                .isSaleprice(node.get("isSOPriceList").asText())
                                .orgId(node.get("priceListOrg").asInt())
                                .name(node.get("namePriceList").asText())
                                .erpPriceListId(node.get("M_PriceList_ID").asInt())
                                .fromDate(node.get("validFrom").asText())
                                .isActive(node.get("isActivePriceList").asText())
                                .build();
                        posTerminalDto.setPriceListIntDto(priceInt);
                        listPos.add(posTerminalDto);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }

        return listPos;
    }

    public List<PosTerminalDto> getPosTerminalERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp ERPNext **");
        credential.setOutputFormatter("yyyy-MM-dd");
        List<PosTerminalDto> listPos = new ArrayList<>();
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        IntPartnerCredential intPosCredential = IntPartnerCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "token " + tokenERPNextRespDto.getApiKey() + ":" + tokenERPNextRespDto.getApiSecret());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String url = tokenERPNextRespDto.getUrl() +"/api/method/"+ commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_PTM)  ;
        UriComponentsBuilder uriBuild = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("type",intPosCredential.getIntType());

        if(intPosCredential.getIntType().equals("UPDATE"))
        {
            uriBuild.queryParam("data",intPosCredential.getIntDate());
        }
        URI uri = uriBuild.build().encode().toUri();
        log.info("url integration posterminal: " + uri );
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<PosTerminalDto> listPosTerminal = new ArrayList<>();
        try {
            responseErpNext = externalRestTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("message");
                for (JsonNode item : data) {
                    PosTerminalDto posTerminalDto = PosTerminalDto.builder()
                            .orgName(item.path("company").asText())
                            .name(item.path("name").asText())
                            .warehouseName(item.path("warehouse").asText())
                            .salesRepName(item.path("user").asText(""))
                            .priceListName(item.path("selling_price_list").asText(""))
                            .isActive(item.path("disabled").asText().equals("0") ? "Y" : "N")
                            .erpPtmName(item.path("name").asText())
                            .printerIp(item.path("ip_printer").asText(""))
                            .isRestaurant(item.path("is_restaurant").asText().equals("1") ? "Y" : "N")
                            .isNotifyBill(item.path("is_notify_bill").asText().equals("1") ? "Y" : "N")
                            .isBillMerge(item.path("is_bill_merge_items").asText().equals("1") ? "Y" : "N")
                            .build();
                    listPos.add(posTerminalDto);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return listPos;
    }
}
