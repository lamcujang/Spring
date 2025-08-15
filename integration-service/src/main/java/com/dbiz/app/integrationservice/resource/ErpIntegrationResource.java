package com.dbiz.app.integrationservice.resource;


import com.dbiz.app.integrationservice.constant.AppConstant;
import com.dbiz.app.integrationservice.service.*;
import org.common.dbiz.dto.integrationDto.ErpIntegrationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.shiftInt.ShiftIntDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.ErpIntegrationQueryRequest;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/erpIntegration"})
@Slf4j
@RequiredArgsConstructor
public class ErpIntegrationResource {

//    private final ErpIntegrationService erpIntegrationService;

    private final ErpIntegrationServiceV2 erpIntegrationService;
    private final ErpNextIntegrationService erpNextIntegrationService;
    private final ProductIntegrationService productIntegrationService;
    private final ShiftIntegrationService shiftIntegrationService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute ErpIntegrationQueryRequest request) {
        log.info("*** ErpIntegration, resource; findAll erpIntegration ***");

        return this.erpIntegrationService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ErpIntegrationDto erpIntegrationDto) {
        log.info("*** ErpIntegration, resource; save erpIntegration: {} ***", erpIntegrationDto);

        return this.erpIntegrationService.save(erpIntegrationDto);
    }

    @PostMapping("/syncIntegration")
    public ResponseEntity<GlobalReponse> syncIntegration(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** ErpIntegration, resource; syncIntegration: {} ***", credential);
//        try {

        return ResponseEntity.ok(this.erpIntegrationService.syncIntegration(credential));
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("Error: {}", e.getMessage());
//            return ResponseEntity.ok(GlobalReponse.builder()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .message(e.getMessage())
//                    .errors(e.getMessage())
//                    .build());
//        }
    }

    @GetMapping("/testApi")
    public GlobalReponse testApi() {
        log.info("*** ErpIntegration, resource; testApi ***");

        return this.erpNextIntegrationService.testApi();
    }

    @GetMapping("/syncPosInvoice")
    public ResponseEntity<GlobalReponse> syncPosInvoice(@RequestParam("posOrderId") Integer posInteger, @RequestParam("orgId") Integer orgId) {
        log.info("*** ErpIntegration, resource; syncPosInvoice ***");

        return ResponseEntity.ok(this.erpNextIntegrationService.syncPosInvoice(posInteger, orgId));
    }

    @PostMapping("/syncShiftIntegration")
    public ResponseEntity<GlobalReponse> syncShiftIntegration(@RequestBody ShiftIntDto credential) {
        log.info("*** ErpIntegration, resource; syncShiftIntegration: {} ***", credential);
        return ResponseEntity.ok(this.shiftIntegrationService.syncApi(credential));
    }

    @PostMapping("/syncImage")
    public ResponseEntity<GlobalReponse> syncImage(@RequestBody SyncIntegrationCredential credential) {
        log.info("*** ErpIntegration, resource; syncImage: {} ***", credential);
        return ResponseEntity.ok(this.productIntegrationService.productIntegration(credential));}


    @PostMapping("/handLeEx")
    public ResponseEntity<GlobalReponse> handleEx(@RequestBody List<UserDto> credential) {
        log.info("*** ErpIntegration, resource; handleEx: {} ***", credential);
        return ResponseEntity.ok(this.erpNextIntegrationService.handleEx(credential));
    }

    @PostMapping("/activeToggle")
    public GlobalReponse activeToggle(@RequestBody ErpIntegrationDto erpIntegrationDto) {
        log.info("*** ErpIntegration, resource; save erpIntegration: {} ***", erpIntegrationDto);

        return this.erpIntegrationService.activeToggle(erpIntegrationDto);
    }
}
