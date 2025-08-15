package com.dbiz.app.integrationservice.resource;


import com.dbiz.app.integrationservice.domain.ErpIntegration;
import com.dbiz.app.integrationservice.service.PartnerIntegrationService;
import org.common.dbiz.dto.integrationDto.IntegrationHistoryDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.IntegrationHistoryQueryRequest;
import com.dbiz.app.integrationservice.service.IntegrationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/integrationHistory"})
@Slf4j
@RequiredArgsConstructor
public class IntegrationHistoryResource {
    private final IntegrationHistoryService service;

    private final PartnerIntegrationService partnerIntegrationService;
    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute IntegrationHistoryQueryRequest request) {
        log.info("*** IntegrationHistory, resource; fetch all integrationHistory ***");

        return this.service.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody IntegrationHistoryDto erpIntegrationDto) {
        log.info("*** ErpIntegration, resource; save erpIntegration: {} ***", erpIntegrationDto);

        return this.service.save(erpIntegrationDto);
    }

    @PostMapping("/saveAll")
    public GlobalReponse saveAll(@RequestBody List<IntegrationHistoryDto> erpIntegrationDto) {
        log.info("*** ErpIntegration, resource; save erpIntegration: {} ***", erpIntegrationDto);

        return this.service.saveAll(erpIntegrationDto);
    }

    @GetMapping("/testInt")
    public ResponseEntity<GlobalReponse> testInt(@RequestParam(value = "testInt", required = false) String testInt) {
        log.info("*** IntegrationHistory, resource; testInt ***");

        return this.service.testInt(testInt);
    }

    @PostMapping("/testIntParner")
    public ResponseEntity<GlobalReponse> testIntPartner(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** IntegrationHistory, resource; testInt ***");
        //SyncIntegrationCredential credential, ErpIntegration erpIntegration
        ErpIntegration erpIntegrationResource = new ErpIntegration();
        return ResponseEntity.ok(this.partnerIntegrationService.getPartnerERP(credential,erpIntegrationResource));
    }
}
