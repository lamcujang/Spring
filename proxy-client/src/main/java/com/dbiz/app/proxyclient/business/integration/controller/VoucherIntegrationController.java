package com.dbiz.app.proxyclient.business.integration.controller;


import com.dbiz.app.proxyclient.business.integration.service.VoucherIntegrationClientSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.voucher.CheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntCheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.CheckInVoucherDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voucher")
@Slf4j
@RequiredArgsConstructor
public class VoucherIntegrationController {

    private final VoucherIntegrationClientSerivce voucherIntegrationService;

    @PostMapping("/checkVoucherInfo")
    public ResponseEntity<GlobalReponse> checkVoucherInfo(@RequestBody CheckVoucherInfoDto dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.checkVoucherInfo(dto)).getBody();
    }

    @GetMapping("/getVoucherServices")
    public ResponseEntity<GlobalReponse> getVoucherServices(@SpringQueryMap VoucherParamDto dto) {
        log.info("** getVoucherServices **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherServices(dto)).getBody();
    }

    @GetMapping("/getVoucherServiceOrders")
    public ResponseEntity<GlobalReponsePagination> getVoucherServiceOrders(@SpringQueryMap  VoucherParamDto dto) {
        log.info("** getVoucherServiceOrders **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherServiceOrders(dto)).getBody();
    }

    @PostMapping("/getVoucherInfo")
    public ResponseEntity<GlobalReponse> geyVoucherInfo(@RequestBody CheckVoucherInfoDto dto) {
        log.info("** getVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherInfo(dto)).getBody();
    }

    @PostMapping("/checkInVoucher")
    public ResponseEntity<GlobalReponse> checkInVoucher(@RequestBody CheckInVoucherDto dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.checkInVoucher(dto)).getBody();
    }

    @PostMapping("/test")
    public ResponseEntity<GlobalReponse> test(@RequestBody SyncIntegrationCredential dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.test(dto)).getBody();
    }
}
