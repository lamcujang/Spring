package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.constant.AppConstant;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final MessageSource messageSource;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Override
    public String getSysValueByName(String name) {
        log.info("** getEndPointINT **");
        StringBuilder endPoint = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("tenantId", AuditContext.getAuditInfo().getTenantId() + "");

        HttpEntity<String> entityHeader = new HttpEntity<>(headers);
        GlobalReponse callExService = restTemplate.exchange(AppConstant.DiscoveredDomainsApi.SYSTEM_SERVICE_API_URL_GET_BY_NAME + "/" + name,
                HttpMethod.GET,entityHeader, GlobalReponse.class).getBody();
        if (callExService.getStatus() == HttpStatus.OK.value()) {
            endPoint = new StringBuilder(callExService.getData().toString());
        } else {
            throw new PosException(messageSource.getMessage("get.endpoint.error", null, LocaleContextHolder.getLocale()));
        }

        return callExService.getData().toString();
    }
}
