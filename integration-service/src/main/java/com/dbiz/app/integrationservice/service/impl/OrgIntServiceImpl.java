package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.OrgIntService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.OrgIntDto;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntOrgCredential;
import org.common.dbiz.request.intergrationRequest.external.IntPartnerCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrgIntServiceImpl implements OrgIntService {
    private final MessageSource messageSource;

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final ObjectMapper objectMapper;
    private StringBuilder error;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    /**
     *
     * @param credential
     * @return
     */
    @Override
    public IntegrationHistory orgIntegration(SyncIntegrationCredential credential) {
        StringBuilder message = new StringBuilder();
        GlobalReponse response = new GlobalReponse();
        log.info("**ServiceImpl::layer -  orgIntegration **");
        try {
            ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
            if (erpIntegration == null) {
                throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
            }
            List<OrgDto> getOrgErp = null;
            OrgIntDto intOrgDto = null;
            if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
                getOrgErp = this.getOrgErpIdempiere(credential, erpIntegration);
                intOrgDto = OrgIntDto.builder()
                        .listOrgInt(getOrgErp)
                        .type(AppConstant.ERP_PLATFORM_IDEMPIERE)
                        .build();
            } else if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
                getOrgErp = this.getOrgErpERPNext(credential, erpIntegration);
                intOrgDto = OrgIntDto.builder()
                        .listOrgInt(getOrgErp)
                        .type(AppConstant.ERP_PLATFORM_ERPNEXT)
                        .build();
            }
            response = commonIntegrationService.sendPostRequest(AppConstant.DiscoveredDomainsApi.TENANT_SERVICE_API_INTEGRATION_ORG, intOrgDto, GlobalReponse.class);
            if (response.getStatus() != HttpStatus.OK.value()) {
                message.append(response.getErrors());
            }
            message.append(response.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.append(e.getMessage());
        }
        return IntegrationHistory.builder()
                .intType(credential.getDataType())
                .intFlow(credential.getIntFlow())
                .intDate(Instant.now())
                .intStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI")
                .userId(credential.getUserId())
                .tenantId(AuditContext.getAuditInfo().getTenantId())
                .orgId(0)
                .description(message.toString())
                .id(credential.getIntegrationHistoryId())
                .build();
    }

    // lay org tu erp

    public List<OrgDto> getOrgErpIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp Idempiere**");
        List<OrgDto> listOrg = new ArrayList<>();
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntOrgCredential intOrgCredential = IntOrgCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .orgId(erpIntegration.getOrgId())
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(commonIntegrationService.getTokenIdempiere().getToken());


        try {
            HttpEntity<IntOrgCredential> requestEntity = new HttpEntity<>(intOrgCredential, headers);
            String url = erpIntegration.getErpUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_ORG);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode root = objectMapper.readTree(responseBody);

                    for (JsonNode node : root.path("data")) {

                        OrgDto orgDto = OrgDto.builder()
                                .name(node.path("orgName").asText())
                                .address(node.path("address").asText().replace("+", " "))
                                .phone(node.path("phone").asText())
                                .code(node.path("orgCode").asText())
                                .taxCode(node.path("taxId").asText())
                                .erpOrgId(node.path("AD_Org_ID").asInt())
                                .isSummary(node.path("isSummary").asText())
                                .isActive(node.path("isActive").asText())
                                .isPosMng("Y")
                                .build();
                        String address = node.path("address").asText().replace("+", " ");
                        orgDto.setAddress(address);
                        listOrg.add(orgDto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (ResourceAccessException e) {
            throw new PosException(messageSource.getMessage("erp.timeout", null, LocaleContextHolder.getLocale()));
        }

        return listOrg;
    }

    public List<OrgDto> getOrgErpERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getOrgErp ERPNext**");
        credential.setOutputFormatter("yyyy-MM-dd");
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        IntPartnerCredential intOrgCredential = IntPartnerCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
//        headers.set("Authorization", "token " + tokenERPNextRespDto.getApiKey() + ":" + tokenERPNextRespDto.getApiSecret());

        headers.add("Cookie", commonIntegrationService.getCookie(",","=",""));

        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        StringBuilder url = new StringBuilder(tokenERPNextRespDto.getUrl() + "/api/method/" + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_ORG));
        URI uri = UriComponentsBuilder.fromHttpUrl(url.toString())
                .queryParam("type", intOrgCredential.getIntType())
                .queryParam("date", intOrgCredential.getIntDate())
                .build().encode().toUri();
        log.info("url integration org: " + uri);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<OrgDto> listOrg = new ArrayList();
        try {
            ResponseEntity<String> responseErpNext = externalRestTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("message");
                for (JsonNode item : data) {
                    StringBuilder address = new StringBuilder(item.path("address_line1").asText() == null ? "" : item.path("address_line1").asText());
                    address.append(item.path("address_line2").asText() == null ? "" : item.path("address_line2").asText());
                    OrgDto orgDto = OrgDto.builder()
                            .code(item.path("abbr").asText())
                            .name(item.path("company_name").asText())
                            .isPosMng("N")
                            .taxCode(item.path("tax_id").asText(""))
                            .phone(item.path("phone_no").asText(""))
                            .isActive(item.path("disabled").asText().equals("0") ? "Y" : "N")
                            .address(item.path("address_line1").asText("") + " " + item.path("address_line2").asText(""))
                            .isSummary(item.path("is_group").asText().equals("0") ? "N" : "Y")
                            .erpOrgName(item.path("company_name").asText())
                            .build();
                    listOrg.add(orgDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listOrg;
    }
}
