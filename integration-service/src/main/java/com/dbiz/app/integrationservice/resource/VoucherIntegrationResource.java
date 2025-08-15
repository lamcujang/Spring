package com.dbiz.app.integrationservice.resource;


import com.dbiz.app.integrationservice.service.OrderIntegrationService;
import com.dbiz.app.integrationservice.service.VoucherIntegrationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.integrationDto.voucher.CheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.IntCheckVoucherInfoDto;
import org.common.dbiz.dto.integrationDto.voucher.VoucherParamDto;
import org.common.dbiz.dto.integrationDto.voucher.checkin.CheckInVoucherDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.intergrationRequest.SyncIntegrationCredential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/api/v1/voucher"})
@Slf4j
@RequiredArgsConstructor
public class VoucherIntegrationResource {

    private final VoucherIntegrationService voucherIntegrationService;

    private final OrderIntegrationService orderIntegrationService;


    @PostMapping("/checkVoucherInfo")
    public ResponseEntity<GlobalReponse> checkVoucherInfo(@RequestBody CheckVoucherInfoDto dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.checkVoucherInfo(dto));
    }

    @GetMapping("/getVoucherServices")
    public ResponseEntity<GlobalReponse> getVoucherServices(@ModelAttribute  VoucherParamDto dto) {
        log.info("** getVoucherServices **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherServices(dto));
    }

    @GetMapping("/getVoucherServiceOrders")
    public ResponseEntity<GlobalReponsePagination> getVoucherServiceOrders(@ModelAttribute  VoucherParamDto dto) {
        log.info("** getVoucherServiceOrders **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherServiceOrders(dto));
    }

    @PostMapping("/getVoucherInfo")
    public ResponseEntity<GlobalReponse> geyVoucherInfo(@RequestBody CheckVoucherInfoDto dto) {
        log.info("** getVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.getVoucherInfo(dto));
    }

    @PostMapping("/checkInVoucher")
    public ResponseEntity<GlobalReponse> checkInVoucher(@RequestBody CheckInVoucherDto dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.voucherIntegrationService.checkInVoucher(dto));
    }

    @PostMapping("/test")
    public ResponseEntity<GlobalReponse> test(@RequestBody SyncIntegrationCredential dto) {
        log.info("** checkVoucherInfo **");
        return ResponseEntity.ok(this.orderIntegrationService.syncOrderIntegration(dto));
    }

}
