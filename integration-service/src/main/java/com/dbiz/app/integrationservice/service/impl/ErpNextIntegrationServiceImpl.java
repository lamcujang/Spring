package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.ErpIntegrationService;
import com.dbiz.app.integrationservice.service.ErpNextIntegrationService;
import com.dbiz.app.integrationservice.service.OrderIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.external.IntProductCategoryCredential;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional
@SuppressWarnings("unchecked")
public class ErpNextIntegrationServiceImpl implements ErpNextIntegrationService {

    private final CommonIntegrationService commonIntegrationService;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final OrderIntegrationService orderIntegrationService;

    private final EntityManager entityManager;

    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    /**
     *
     * @return
     */
    @Override
    public GlobalReponse testApi() {
        log.info("** testApi **");
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        String urlEndPoint = tokenERPNextRespDto.getUrl() + commonIntegrationService.getEndPointINT("D_END_INT_RESOURCE") + "/Company?fields=[\"name\",\"default_currency\",\"country\" ]";
        log.info("urlEndPoint: {}", urlEndPoint);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "token " + tokenERPNextRespDto.getApiKey() + ":" + tokenERPNextRespDto.getApiSecret());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<IntProductCategoryCredential> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = externalRestTemplate.exchange(urlEndPoint, HttpMethod.GET, requestEntity, String.class);


        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("response: {}", response.getBody());
                return GlobalReponse.builder().status(200).message("Success").data(response.getBody()).build();
            } else {
                log.error("response: {}", response.getBody());
                return GlobalReponse.builder().status(200).message("Error").data(response.getBody()).build();
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }

        return null;
    }

    /**
     *
     * @param posOrderId
     * @return
     */
    @Override
    public GlobalReponse syncPosInvoice(Integer posOrderId, Integer orgId) {
        log.info("** syncPosInvoice **");

        return orderIntegrationService.syncInvoice(posOrderId, orgId);
    }

    /**
     *
     * @param credential
     * @param erpIntegration
     * @return
     */
    @Override
    public GlobalReponse syncCustomer(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        return null;
    }

    /**
     *
     * @param credential
     * @return
     */
    @Override
    public GlobalReponse handleEx(List<UserDto> credential) {
        log.info("** handleEx **");
        GlobalReponse response = this.commonIntegrationService.sendPostRequest("http://USER-SERVICE/user-service/api/v1/users/handleEx", credential,GlobalReponse.class);
        String status = response.getStatus() == 200 ? "Success" : "Error";
        return GlobalReponse.builder().status(response.getStatus()).message(status).data(response.getData()).build();
    }
}
