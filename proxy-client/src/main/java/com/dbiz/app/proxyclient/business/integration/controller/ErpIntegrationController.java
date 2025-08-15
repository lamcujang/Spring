package com.dbiz.app.proxyclient.business.integration.controller;

import com.dbiz.app.proxyclient.business.integration.service.ErpIntegrationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/erpIntegration")
@Slf4j
@RequiredArgsConstructor
public class ErpIntegrationController {

    private final ErpIntegrationClientService clientService;
    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute ErpIntegrationQueryRequest request) {
        log.info("*** ErpIntegration, resource; findAll erpIntegration ***");

        return ResponseEntity.ok(this.clientService.findAll(request).getBody());
    }
    @PostMapping("/save")
    public ResponseEntity<GlobalReponse>  save(@RequestBody ErpIntegrationDto  erpIntegrationDto){
        log.info("*** ErpIntegration , resource; save erpIntegration ***");
        return ResponseEntity.ok(this.clientService.save(erpIntegrationDto).getBody());
    }

    @PostMapping("/syncIntegration")
    public ResponseEntity<GlobalReponse> syncIntegration(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** ErpIntegration, resource; syncIntegration: {} ***", credential);

        return ResponseEntity.ok(this.clientService.syncIntegration(credential).getBody());
    }

    @GetMapping("/testApi")
    public GlobalReponse testApi() {
        log.info("*** ErpIntegration, resource; testApi ***");
        return this.clientService.testApi();
    }

    @GetMapping("/syncPosInvoice")
    public ResponseEntity<GlobalReponse> syncPosInvoice(@RequestParam("posOrderId")Integer posInteger,@RequestParam("orgId")Integer orgId) {
        log.info("*** ErpIntegration, resource; syncPosInvoice ***");

        return this.clientService.syncPosInvoice(posInteger,orgId);
    }

    @PostMapping("/syncShiftIntegration")
    public ResponseEntity<GlobalReponse> syncShiftIntegration(@RequestBody ShiftIntDto credential) {
        log.info("*** ErpIntegration, resource; syncShiftIntegration: {} ***", credential);
        return this.clientService.syncShiftIntegration(credential);
    }
    @PostMapping("/syncImage")
    public ResponseEntity<GlobalReponse> syncImageProduct(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** ErpIntegration, resource; syncShiftIntegration: {} ***", credential);
        return this.clientService.syncImageProduct(credential);
    }

    @PostMapping("/handLeEx")
    public ResponseEntity<GlobalReponse> handleEx(@RequestBody List<UserDto> credential) {
        log.info("*** ErpIntegration, resource; handleEx: {} ***", credential);
        return this.clientService.handleEx(credential);
    }

    @PostMapping("/activeToggle")
    public ResponseEntity<GlobalReponse>  activeToggle(@RequestBody ErpIntegrationDto  erpIntegrationDto){
        log.info("*** ErpIntegration , resource; activeToggle erpIntegration ***");
        return ResponseEntity.ok(this.clientService.activeToggle(erpIntegrationDto).getBody());
    }
}
