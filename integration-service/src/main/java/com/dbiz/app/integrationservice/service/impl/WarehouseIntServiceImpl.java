package com.dbiz.app.integrationservice.service.impl;

import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.domain.IntegrationHistory;
import com.dbiz.app.integrationservice.helper.DateHelper;
import com.dbiz.app.integrationservice.repository.ErpIntegrationRepository;
import com.dbiz.app.integrationservice.service.CommonIntegrationService;
import com.dbiz.app.integrationservice.service.WarehouseIntService;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.token.TokenERPNextRespDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.common.dbiz.request.intergrationRequest.WarehouseIntDto;
import org.common.dbiz.request.intergrationRequest.external.IntPartnerCredential;
import org.common.dbiz.request.intergrationRequest.external.IntWarehouseCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseIntServiceImpl implements WarehouseIntService {
    private final MessageSource messageSource;

    private final ErpIntegrationRepository erpIntegrationRepository;

    private final CommonIntegrationService commonIntegrationService;

    private final ObjectMapper objectMapper ;

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
    public IntegrationHistory warehouseIntegration(SyncIntegrationCredential credential) {
        StringBuilder error = new StringBuilder();
        log.info("**ServiceImpl::layer -  warehouse integration **");

        IntegrationHistory integrationHistory = new IntegrationHistory();
        ErpIntegration erpIntegration = erpIntegrationRepository.findByIsDefault("Y");
        WarehouseIntDto getWarehouseErp = new WarehouseIntDto();
        GlobalReponse response = null;
        if (erpIntegration == null) {
            throw new PosException(messageSource.getMessage("erp.integrationurl.not.found", null, LocaleContextHolder.getLocale()));
        }
        if (erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_IDEMPIERE) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS)) {
            getWarehouseErp.setListWarehouseDto(getWarehouseIdempiere(credential, erpIntegration));
            getWarehouseErp.setType(AppConstant.ERP_PLATFORM_IDEMPIERE);
            response =commonIntegrationService.sendPostRequest(
                    AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_INT_WAREHOUSE,
                    getWarehouseErp,
                    GlobalReponse.class
            );
        }
        else if(erpIntegration.getErpPlatform().equals(AppConstant.ERP_PLATFORM_ERPNEXT) && credential.getIntFlow().equals(AppConstant.FLOW_ERP_TO_POS))
        {
            int limitStart = 0;
            int limitEnd= 20;
            while (true)
            {
                getWarehouseErp = new WarehouseIntDto();
                List<WarehouseDto> warehouseList = getWarehouseERPNext(credential, erpIntegration, limitStart, limitEnd);
                getWarehouseErp.setListWarehouseDto(warehouseList);
                getWarehouseErp.setType(AppConstant.ERP_PLATFORM_ERPNEXT);
                response =commonIntegrationService.sendPostRequest(
                        AppConstant.DiscoveredDomainsApi.PRODUCT_SERVICE_INT_WAREHOUSE,
                        getWarehouseErp,
                        GlobalReponse.class
                );
                limitStart += limitEnd;
                if (warehouseList.isEmpty() || warehouseList.size() < limitEnd)
                    break;
            }
        }

        if (response.getStatus() != HttpStatus.OK.value()) {
            error = new StringBuilder(response.getErrors());
        }
        integrationHistory.setIntType(credential.getDataType());
        integrationHistory.setIntFlow(credential.getIntFlow());
        integrationHistory.setIntDate(credential.getIntDate() == null ? Instant.now() : DateHelper.toInstantDateAndTime(credential.getIntDate()));
        integrationHistory.setIntStatus(response.getStatus() == HttpStatus.OK.value() ? "COM" : "FAI");
        integrationHistory.setUserId(credential.getUserId());
        integrationHistory.setTenantId(AuditContext.getAuditInfo().getTenantId());
        integrationHistory.setOrgId(0);
        integrationHistory.setDescription(error.toString());
        integrationHistory.setPayload(Json.pretty(getWarehouseErp));
        integrationHistory.setResponse(Json.pretty(response));
        integrationHistory.setId(credential.getIntegrationHistoryId());
        return integrationHistory;
    }


    public List<WarehouseDto> getWarehouseIdempiere(SyncIntegrationCredential credential, ErpIntegration erpIntegration) {
        log.info("** getWarehouseErp **");
        List<WarehouseDto> warehouseDtos = new ArrayList<>();
//        - 1 là từ ngày tích hợp thành công gần nhất, 2 là chọn ngày
        IntWarehouseCredential intWarehouseCredential = IntWarehouseCredential.builder()
                .clientId(erpIntegration.getAdClientId())
                .intType(credential.getIntType())
                .intDate( commonIntegrationService.castIntDate(credential)).build();


        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");  // Đặt Content-Type với charset
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(commonIntegrationService.getTokenIdempiere().getToken());

        try {
            HttpEntity<IntWarehouseCredential> requestEntity = new HttpEntity<>(intWarehouseCredential, headers);
            String url = erpIntegration.getErpUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.name_GetConfig_INT_WHO);
            log.info("url integration org: " + url);
            ResponseEntity<String> response = externalRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode root = objectMapper.readTree(responseBody);
                    for (JsonNode node : root.path("data")) {
                        WarehouseDto warehouseDto = WarehouseDto.builder()
                                .name(node.path("name").asText())
                                .isActive(node.path("isActive").asText())
                                .code(node.path("value").asText())
                                .erpWarehouseId(node.path("M_Warehouse_ID").asInt())
                                .code(node.path("value").asText())
                                .printerIp(node.path("printerHost").asText())
                                .orgId(node.path("AD_Org_ID").asInt())
                                .description(node.path("description").asText())
                                .isNegative(node.path("isDisallowNegativeInv").asText().equals("Y") ? "N" : "Y")
//                                .address(node.path("address").asText())
                                .build();

                        String address = node.path("address").asText().replace("+", " ");
                        warehouseDto.setAddress(address);
                        warehouseDtos.add(warehouseDto);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PosException(messageSource.getMessage("error_internal_gettoken_erp", null, LocaleContextHolder.getLocale()));
        }

        return warehouseDtos;
    }

    public List<WarehouseDto> getWarehouseERPNext(SyncIntegrationCredential credential, ErpIntegration erpIntegration,Integer limitStart,Integer limitEnd) {
        TokenERPNextRespDto tokenERPNextRespDto = commonIntegrationService.getTokenERPNext();
        credential.setOutputFormatter("yyyy-MM-dd");
        IntPartnerCredential intWarehouseCredential = IntPartnerCredential.builder()
                .intType(credential.getIntType())
                .intDate(commonIntegrationService.castIntDate(credential)).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.add("Cookie", commonIntegrationService.getCookie(",","=",""));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String url = tokenERPNextRespDto.getUrl() + commonIntegrationService.getEndPointINT(AppConstant.ErpIntegrationEndPoint.D_ERPNEXT_END_INT_RESOURCE) + "/" + AppConstant.ERPNextDoctype.D_WAREHOUSE;
        UriComponentsBuilder uriBuild = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("fields","[\"*\"]");
        StringBuilder filter = new StringBuilder("[[\"is_voucher\",\"=\",\"0\"],[\"pos_used\",\"=\",\"1\"]");
        if(intWarehouseCredential.getIntType().equals("UPDATE"))
            filter.append(",[\"modified\", \">=\", \""+intWarehouseCredential.getIntDate()+"\"]");
        filter.append("]");
        uriBuild.queryParam("filters",filter.toString());
        uriBuild.queryParam("limit_start",limitStart);
        uriBuild.queryParam("limit_page_length",limitEnd);
        URI uri = uriBuild.build().encode().toUri();
        log.info("url integration org: " + uri );
        ResponseEntity<String> responseErpNext = null;
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        List<WarehouseDto> listWarehouse = new ArrayList<>();
        try {
            responseErpNext = externalRestTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            if (responseErpNext.getStatusCode().is2xxSuccessful()) {
                String responseBody = responseErpNext.getBody();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode data = root.path("data");
                for(JsonNode item : data)
                {

                    WarehouseDto warehouseDto =  WarehouseDto.builder()
                            .orgName(item.path("company").asText())
                            .code(item.path("search_key").asText().isEmpty() ? item.path("name").asText() : item.path("search_key").asText())
                            .name(item.path("warehouse_name").asText())
                            .ErpWarehouseName(item.path("name").asText())
                            .address(item.path("address_line_1").asText("") + " " + item.path("address_line_2").asText("") + " " + item.path("city").asText("") + " " + item.path("state").asText(""))
                            .description(item.path("description").asText(""))
                            .printerIp(item.path("ip_printer").asText(""))
                            .isNegative(item.path("isDisallowNegativeInv").asText().equals("0") ? "N" : "Y")
                            .isActive(item.path("disabled").asText().equals("0") ? "Y" : "N")
                            .build();
                    listWarehouse.add(warehouseDto);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return listWarehouse;
    }

    public List<WarehouseDto> getJsonWarehouseERPNext(JsonNode param) {
        List<WarehouseDto> listWarehouse = new ArrayList<>();
        for(JsonNode item : param)
        {

            WarehouseDto warehouseDto =  WarehouseDto.builder()
                    .orgName(item.path("company").asText())
                    .code(item.path("search_key").asText())
                    .name(item.path("warehouse_name").asText())
                    .ErpWarehouseName(item.path("name").asText())
                    .address(item.path("address_line_1").asText("") + " " + item.path("address_line_2").asText("") + " " + item.path("city").asText("") + " " + item.path("state").asText(""))
                    .description(item.path("description").asText(""))
                    .printerIp(item.path("ip_printer").asText(""))
                    .isNegative(item.path("isDisallowNegativeInv").asText().equals("0") ? "N" : "Y")
                    .isActive(item.path("disabled").asText().equals("0") ? "Y" : "N")
                    .build();
            listWarehouse.add(warehouseDto);
        }
        return listWarehouse;
    }
}
