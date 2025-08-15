package com.dbiz.app.proxyclient.business.integration.controller;

import com.dbiz.app.proxyclient.business.integration.service.IntegrationHistoryClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/integrationHistory")
@Slf4j
@RequiredArgsConstructor
public class IntegrationHistoryController {

    private final IntegrationHistoryClientService clientService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap IntegrationHistoryQueryRequest  request){
        log.info("*** IntegrationHistory , resource; fetch all ***");
        return ResponseEntity.ok(this.clientService.findAll(request).getBody());
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody IntegrationHistoryDto  erpIntegrationDto){
        log.info("*** IntegrationHistory , resource; save erpIntegration ***");
        return ResponseEntity.ok(this.clientService.save(erpIntegrationDto).getBody());
    }

    @PostMapping("/saveAll")
    public  ResponseEntity<GlobalReponse> saveAll(@RequestBody List<IntegrationHistoryDto> erpIntegrationDto){
        log.info("*** IntegrationHistory , resource; save all erpIntegration ***");
        return ResponseEntity.ok(this.clientService.saveAll(erpIntegrationDto).getBody());
    }

    @GetMapping("/testInt")
    public ResponseEntity<GlobalReponse> testInt(@RequestParam(value = "testInt", required = false) String testInt) {
        log.info("*** IntegrationHistory, resource; testInt ***");

        return this.clientService.testInt(testInt);
    }

    @PostMapping("/testIntParner")
    public ResponseEntity<GlobalReponse> testIntPartner(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** IntegrationHistory, resource; testInt ***");
        return ResponseEntity.ok(this.clientService.testIntPartner(credential).getBody());
    }
}
