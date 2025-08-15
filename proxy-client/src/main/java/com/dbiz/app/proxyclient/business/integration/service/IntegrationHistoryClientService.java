package com.dbiz.app.proxyclient.business.integration.service;


import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "INTEGRATION-SERVICE", contextId = "IntegrationHistoryClientService", path = "/integration-service/api/v1/integrationHistory", decode404 = true
        , configuration = FeignClientConfig.class )
public interface IntegrationHistoryClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap IntegrationHistoryQueryRequest  request);

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse>  save(@RequestBody IntegrationHistoryDto  erpIntegrationDto);

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody List<IntegrationHistoryDto> erpIntegrationDto);

    @GetMapping("/testInt")
    ResponseEntity<GlobalReponse> testInt(@RequestParam(value = "testInt", required = false) String testInt);


    @PostMapping("/testIntParner")
    public ResponseEntity<GlobalReponse> testIntPartner(@RequestBody SyncIntegrationCredential credential);
}
