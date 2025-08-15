package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.GetTokenCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommonIntegrationServiceImpl implements CommonIntegrationService {

    private final ErpIntegrationRepository erpIntegrationRepository;
    private final MessageSource messageSource;
    private final IntegrationHistoryRepository integrationHistoryRepository;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public TokenIdempiereRespDto getTokenIdempiere() {
        log.info("** getTokenErp **");
        StringBuilder tokenResponse = null;
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");

        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        GetTokenCredential token = GetTokenCredential.builder()
                .userName(erpIntegration.getUsername())
                .password(erpIntegration.getPassword())
                .parameters(GetTokenCredential.Parameters.builder()
                        .clientId(erpIntegration.getAdClientId())
                        .roleId(erpIntegration.getAdRoleId())
                        .language("en_US")
                        .build()).build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<GetTokenCredential> requestEntity = new HttpEntity<>(token, headers);
        String urlGetoken = erpIntegration.getErpUrl() + getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_GETTOKEn);
        ResponseEntity<String> response = externalRestTemplate.exchange(urlGetoken, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                JsonNode root = objectMapper.readTree(responseBody);
                String userId = root.path("userId").asText();
                String language = root.path("language").asText(null);
                tokenResponse = new StringBuilder(root.path("token").asText());
                log.info("resultCode: " + userId);
                log.info("errorMessage: " + language);
                log.info("token: " + tokenResponse.toString());
            } catch (Exception e) {
                throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
            }
        }

        return TokenIdempiereRespDto.builder()
                .adClientId(erpIntegration.getAdClientId())
                .url(erpIntegration.getErpUrl())
                .orgId(erpIntegration.getOrgId())
                .token(tokenResponse.toString()).build();
    }

    @Override
    public String getEndPointINT(String nameEnd) {
        log.info("** getEndPointINT **");
        StringBuilder endPoint = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getMainTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        GlobalReponse callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_API_URL_GET_BY_NAME + "/" + nameEnd,HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
        if (callExService.getStatus() == HttpStatus.OK.value()) {
            endPoint = new StringBuilder(callExService.getData().toString());
        } else {
            throw new PosException(messageSource.getMessage("get.endpoint.error", null, LocaleContextHolder.getLocale()));
        }

        return callExService.getData().toString();
    }

    /**
     * @param credential
     * @return
     */
    @Override
    public String castIntDate(SyncIntegrationCredential credential) {
        StringBuilder formattedDate;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(credential.getOutputFormatter());
        if (credential.getFromType() == 2) {
            LocalDateTime dateTime = LocalDateTime.parse(credential.getDate() == null ? credential.getIntDate() : credential.getDate(), inputFormatter);
            formattedDate = new StringBuilder(dateTime.format(outputFormatter));
            return formattedDate.toString();
        } else { // lay ngay tich hop cuoi cung thanh cong
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

    /**
     * @param credential
     * @return
     */
    @Override
    public String castDateYYYYmmDD(SyncIntegrationCredential credential) {
        StringBuilder formattedDate;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (credential.getFromType() == 2) {
            return DateHelper.fromDateTimeToDate2((credential.getDate() == null || credential.getDate().isEmpty()) ? credential.getIntDate() : credential.getDate());
        }
        else
        {
            IntegrationHistory integrationHistory = integrationHistoryRepository.findLatestIntegrationDate(credential.getDataType());
            if (integrationHistory != null) {
                return DateHelper.fromDateTimeToDate2(integrationHistory.getIntDate());
            } else {
                formattedDate = new StringBuilder(LocalDateTime.now().format(outputFormatter));
                return formattedDate.toString();
            }
        }
    }

    /**
     *
     * @param credential
     * @return
     */
    @Override
    public String castDateYYYYDDMM(SyncIntegrationCredential credential) {
        StringBuilder formattedDate;
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        if (credential.getFromType() == 2) {
            return DateHelper.toYYYYDDMM((credential.getDate() == null || credential.getDate().isEmpty()) ? credential.getIntDate() : credential.getDate());
        }
        else
        {
            IntegrationHistory integrationHistory = integrationHistoryRepository.findLatestIntegrationDate(credential.getDataType());
            if (integrationHistory != null) {
                return DateHelper.toYYYYDDMM(integrationHistory.getIntDate());
            } else {
                formattedDate = new StringBuilder(LocalDateTime.now().format(outputFormatter));
                return formattedDate.toString();
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public TokenERPNextRespDto getTokenERPNext() {
        log.info("** get Token ERPNext **");
        ErpIntegration erpIntegrations = erpIntegrationRepository.findFirstByErpPlatformAndIsDefaultAndIsActive("ERPNext", "Y", "Y");

        if (erpIntegrations == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        return TokenERPNextRespDto.builder()
                .url(erpIntegrations.getErpUrl())
                .apiKey(erpIntegrations.getApiKey() )
                .apiSecret(erpIntegrations.getApiSecret()).build();
    }

    /**
     *
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @param <T>
     * @param <R>
     */
    @Override
    public <T, R> R sendPostRequest(String url, T requestBody, Class<R> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", AuditContext.getAuditInfo().getLanguage());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForObject(url, requestEntity, responseType);
    }

    /**
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    @Override
    public String getCookie(String url, String username, String password) {
        ErpIntegration erpIntegrations = erpIntegrationRepository.findFirstByErpPlatformAndIsDefaultAndIsActive("ERPNext", "Y", "Y");

        if (erpIntegrations == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        if(erpIntegrations.getCookies() != null && !erpIntegrations.getCookies().isEmpty()) {
            Pattern pattern = Pattern.compile("Expires=([A-Za-z]+, \\d{1,2} [A-Za-z]+ \\d{4} \\d{2}:\\d{2}:\\d{2} GMT)");
            Matcher matcher = pattern.matcher(erpIntegrations.getCookies());
            if (matcher.find())
            {
                String expiresDateStr = matcher.group(1);
                System.out.println("Ngày hết hạn (UTC): " + expiresDateStr);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
                        .withZone(ZoneId.of("UTC"));
                ZonedDateTime expiresDate = ZonedDateTime.parse(expiresDateStr, formatter);
                ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
                long hoursBetween = Duration.between(now, expiresDate).toHours();
                long minutesBetween = Duration.between(now, expiresDate).toMinutes();
                if (hoursBetween  > 0) {
                    return erpIntegrations.getCookies();
                }
            }
        }



        String requestBody = String.format("{\"usr\":\"%s\", \"pwd\":\"%s\"}",erpIntegrations.getUsername(), erpIntegrations.getPassword());
        HttpHeaders headersC = new HttpHeaders();
        headersC.setContentType(MediaType.APPLICATION_JSON);

        // Tạo request entity
        HttpEntity<String> requestEntityC = new HttpEntity<>(requestBody, headersC);

        // Gửi POST request để login
        ResponseEntity<String> response = externalRestTemplate.exchange(
                "https://hsp.dbiz.com/api/method/login",
                HttpMethod.POST,
                requestEntityC,
                String.class
        );
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> cookies = responseHeaders.get(HttpHeaders.SET_COOKIE);
        String cookie = String.join(";", cookies);
        erpIntegrations.setCookies(cookie);
        erpIntegrationRepository.save(erpIntegrations);
        return cookie;
    }
}
