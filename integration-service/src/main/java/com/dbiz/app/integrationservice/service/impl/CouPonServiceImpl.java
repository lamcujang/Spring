package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.domain.InterfaceIntegration;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.IntegrationHistoryRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.CouPonService;
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
import org.common.dbiz.dto.integrationDto.token.TokenIdempiereRespDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponDto;
import org.common.dbiz.dto.productDto.ProductIntDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CouPonServiceImpl implements CouPonService {

    private final CommonIntegrationService commonIntegrationService;
    @Autowired
    @Qualifier("restTemplateBean")
    private final RestTemplate restTemplate;

    @Autowired
    @Qualifier("externalRestTemplate")
    private final RestTemplate externalRestTemplate;

    @Autowired
    @Qualifier("externalRestTemplateProduct")
    private final RestTemplate externalRestTemplateProduct;

    private final ObjectMapper objectMapper;

    private final IntegrationHistoryRepository integrationHistoryRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final DataSourceContextHolder dataSourceContextHolder;

    private final DataSourceConfigService dataSourceConfigService;

    private final DataSourceRoutingService dataSourceRoutingService;

    /**
     * @return
     */
    @Override
    public IntegrationHistory syncCounponIntegration(SyncIntegrationCredential syncIntegrationCredential) {
        StringBuilder error = new StringBuilder();
        List<CouponDto> couponDtos = getCouPon(syncIntegrationCredential);

        IntegrationHistory integrationHistory = new IntegrationHistory();
        HttpHeaders headers = new HttpHeaders();
        headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
        headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
        headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
        headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
        headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
        headers.set("Accept-Language", LocaleContextHolder.getLocale().toString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<CouponDto>> requestEntity = new HttpEntity<>(couponDtos, headers);

        GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_URL_INT_SAVE, requestEntity, GlobalReponse.class);

        if (response.getStatus() != HttpStatus.OK.value()) {
            error = new StringBuilder(response.getErrors());
            integrationHistory.setDescription(error.toString());
        }
        integrationHistory.setIntType(syncIntegrationCredential.getDataType());
        integrationHistory.setIntFlow(syncIntegrationCredential.getIntFlow());
        integrationHistory.setIntDate(syncIntegrationCredential.getIntDate() != null ? DateHelper.toInstantDateAndTime(syncIntegrationCredential.getIntDate()): Instant.now());
        integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        integrationHistory.setUserId(syncIntegrationCredential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(couponDtos));
        integrationHistory.setResponse(Json.pretty(response));
        integrationHistory.setId(syncIntegrationCredential.getIntegrationHistoryId());
        return integrationHistory;
    }

    public List<CouponDto> getCouPon(SyncIntegrationCredential credential) {
        TokenIdempiereRespDto tokenIdempiereRespDto = commonIntegrationService.getTokenIdempiere();
        List<CouponDto>resultGet = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());

        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
            url.append(commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_COP)).append("?");
            url.append("$filter=").append("AD_Client_ID").append(" eq ").append(tokenIdempiereRespDto.getAdClientId());
            if(credential.getIntType().equals("UPDATE"))
                url.append(" AND Updated_Date ge '").append( commonIntegrationService.castDateYYYYmmDD(credential)).append("'");
            else if(credential.getIntType().equals("INSERT"))
                url.append(" AND IsActive eq true ");
            url.append("&").append("$select=")
                    .append("ad_org_id,coupon_code,couponamt,c_pos_id,c_bpartner_id,c_bpartner2_id,isactive,isavailable");
            url.append("&$top=50");
            log.info("URL: {}", url.toString());
            ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                try {
                    JsonNode root = objectMapper.readTree(responseBody);
                    Integer skipRecords = root.get("skip-records").asInt();
                    Integer rowCount = root.get("row-count").asInt();
                    JsonNode records = root.get("records");
                    if(records!= null)
                    {
                        for (int i = 0; i < records.size(); i++) {
                            JsonNode record = records.get(i);
                            CouponDto couponDto = new CouponDto();
                            couponDto.setErpCouponId(record.get("id").asInt());
                            couponDto.setIsActive(record.get("IsActive").asBoolean() ? "Y" : "N");
                            couponDto.setIsAvailable(record.get("IsAvailable").asBoolean() ? "Y" : "N");
                            couponDto.setBalanceAmount(record.get("CouponAmt").decimalValue());
                            couponDto.setCode(record.get("Coupon_Code").asText());
                            couponDto.setOrgId(record.get("AD_Org_ID").get("id").asInt());
                            if(record.has("C_POS_ID"))
                                couponDto.setPosTerminalId(record.get("C_POS_ID").get("id").asInt());
                            if(record.has("C_BPartner_ID"))
                                couponDto.setVendorId(record.get("C_BPartner_ID").get("id").asInt());
                            if(record.has("C_BPartner2_ID"))
                                couponDto.setCustomerId(record.get("C_BPartner2_ID").asInt());
                            resultGet.add(couponDto);
                        }
                    }
                    if(skipRecords <= rowCount)
                    {
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
                                .tenantId(AuditContext.getAuditInfo().getMainTenantId())
                                .status(credential.getStatus() != null ? credential.getStatus() : "")
                                .error(credential.getError() != null ? credential.getError() : "")
                                .syncKafka(credential.getSyncKafka() != null ? credential.getSyncKafka() : "N")
                                .outputFormatter(credential.getOutputFormatter() != null ? credential.getOutputFormatter() : "dd-MM-yyyy")
                                .skipRows(50)
                                .build();
                        kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_COUPON, creNew);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error when get coupon from erp", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when get coupon from erp", e);
        }

        return resultGet;
    }
    private String castIntDate(SyncIntegrationCredential credential) {
        StringBuilder formattedDate;
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (credential.getFromType() == 2) {
            LocalDateTime dateTime = LocalDateTime.parse(credential.getDate() == null ? credential.getIntDate() : credential.getDate(), inputFormatter);
            formattedDate = new StringBuilder(dateTime.format(outputFormatter));
            return formattedDate.toString();
        } else {
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




    // listen sync coupon kafka
    public int getTenantNumbers() {
        return dataSourceConfigService.getTenantNumbersRedis();
    }

    @KafkaListener(groupId = AppConstant.TopicKafka.GROUP_ID, topics = AppConstant.TopicKafka.TOPIC_SEND_COUPON, containerFactory = "kafkaListenerContainerFactory")
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
            List<CouponDto> couponDtos = getCouPon2(value);
            IntegrationHistory integrationHistory = new IntegrationHistory();
            HttpHeaders headers = new HttpHeaders();
            headers.set("tenantId", AuditContext.getAuditInfo().getMainTenantId().toString());
            headers.set("orgId", AuditContext.getAuditInfo().getOrgId().toString());
            headers.set("createBy", AuditContext.getAuditInfo().getCreateBy());
            headers.set("updateBy", AuditContext.getAuditInfo().getUpdateBy());
            headers.set("userId", AuditContext.getAuditInfo().getUserId().toString());
            headers.set("Accept-Language", "vi");
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<CouponDto>> requestEntity = new HttpEntity<>(couponDtos, headers);

            GlobalReponse response = restTemplate.postForObject(AppConstant.DiscoveredDomainsApi.PAYMENT_SERVICE_API_URL_INT_SAVE, requestEntity, GlobalReponse.class);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            acknowledgment.acknowledge();
        }
    }



    public List<CouponDto> getCouPon2(SyncIntegrationCredential credential) {
        TokenIdempiereRespDto tokenIdempiereRespDto = commonIntegrationService.getTokenIdempiere();
        List<CouponDto>resultGet = new ArrayList<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenIdempiereRespDto.getToken());

        try {
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            StringBuilder url = new StringBuilder(tokenIdempiereRespDto.getUrl());
            url.append(commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_COP)).append("?");
            url.append("$filter=").append("AD_Client_ID").append(" eq ").append(tokenIdempiereRespDto.getAdClientId());
            if(credential.getIntType().equals("UPDATE"))
                url.append(" AND Updated_Date ge '").append( commonIntegrationService.castDateYYYYmmDD(credential)).append("'");
            else if(credential.getIntType().equals("INSERT"))
                url.append(" AND IsActive eq true ");
            url.append("&").append("$select=")
                    .append("ad_org_id,coupon_code,couponamt,c_pos_id,c_bpartner_id,c_bpartner2_id,isactive,isavailable");
            url.append("&$top=50");
            url.append("&$skip=").append(credential.getSkipRows() != null ? credential.getSkipRows() : 0);
            log.info("URL: {}", url.toString());
            ResponseEntity<String> response = externalRestTemplateProduct.exchange(url.toString(), HttpMethod.GET, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                try {
                    JsonNode root = objectMapper.readTree(responseBody);
                    Integer skipRecords = root.get("skip-records").asInt();
                    Integer rowCount = root.get("row-count").asInt();
                    JsonNode records = root.get("records");
                    if(records!= null)
                    {
                        for (int i = 0; i < records.size(); i++) {
                            JsonNode record = records.get(i);
                            CouponDto couponDto = new CouponDto();
                            couponDto.setErpCouponId(record.get("id").asInt());
                            couponDto.setIsActive(record.get("IsActive").asBoolean() ? "Y" : "N");
                            couponDto.setIsAvailable(record.get("IsAvailable").asBoolean() ? "Y" : "N");
                            couponDto.setBalanceAmount(record.get("CouponAmt").decimalValue());
                            couponDto.setCode(record.get("Coupon_Code").asText());
                            couponDto.setOrgId(record.get("AD_Org_ID").get("id").asInt());
                            if(record.has("C_POS_ID"))
                                couponDto.setPosTerminalId(record.get("C_POS_ID").get("id").asInt());
                            if(record.has("C_BPartner_ID"))
                                couponDto.setVendorId(record.get("C_BPartner_ID").get("id").asInt());
                            if(record.has("C_BPartner2_ID"))
                                couponDto.setCustomerId(record.get("C_BPartner2_ID").asInt());
                            resultGet.add(couponDto);
                        }
                    }
                    if(skipRecords <= rowCount)
                    {
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
                                .skipRows(credential.getSkipRows() != null ? credential.getSkipRows() + 50 : 50)
                                .build();
                        kafkaTemplate.send(AppConstant.TopicKafka.TOPIC_SEND_COUPON, creNew);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error when get coupon from erp", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when get coupon from erp", e);
        }

        return resultGet;
    }
}
