package com.dbiz.app.proxyclient.business.payment.controller;


import com.dbiz.app.proxyclient.business.payment.service.QRCodeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.CheckQRCodeMBBDto;
import org.common.dbiz.dto.paymentDto.QRCodeDto;
import org.common.dbiz.dto.paymentDto.QRCodeMBBDto;
import org.common.dbiz.dto.paymentDto.napas.CheckNapasQRCodeDto;
import org.common.dbiz.dto.paymentDto.request.QRCodeReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/qrcode")
@Slf4j
@RequiredArgsConstructor
public class QRCodeController {

    private final QRCodeClientService qrcodeClientService;

    @PostMapping("/generateMBB")
    public ResponseEntity<GlobalReponse> generateQRCodeMBB(@RequestBody @Valid final QRCodeMBBDto DTO) {
        log.info("*** QRCodeMBBDto, resource; generate QR Code MBB *");
        return ResponseEntity.ok(this.qrcodeClientService.generateQRCodeMBB(DTO)).getBody();
    }

    @PostMapping("/checkOrderMBB")
    public ResponseEntity<GlobalReponse > checkQRCodeMBB(@RequestBody @Valid final CheckQRCodeMBBDto DTO) {
        log.info("*** QRCodeMBBDto, resource; CHECK ORDER QR Code MBB *");
        return ResponseEntity.ok(this.qrcodeClientService.checkQRCodeMBB(DTO)).getBody();
    }

    @PostMapping("/generate")
    public ResponseEntity<GlobalReponse > generateQRCode(@RequestBody @Valid final QRCodeDto DTO) {
        log.info("*** QRCodeDto, resource; GENERATE QR Code *");
        return ResponseEntity.ok(this.qrcodeClientService.generateQRCode(DTO)).getBody();
    }

    @PostMapping("/generateByBankAccount")
    public ResponseEntity<GlobalReponse > generateQRCodeByBank(@RequestBody @Valid final QRCodeDto DTO) {
        log.info("*** QRCodeDto, resource; GENERATE QR Code *");
        return ResponseEntity.ok(this.qrcodeClientService.generateQRCodeByBank(DTO)).getBody();
    }

    @PostMapping("/napas/generate")
    public ResponseEntity<GlobalReponse > generateNapasQRCode(@RequestBody @Valid final QRCodeReqDto DTO) {
        log.info("*** QRCodeDto, resource; GENERATE NAPAS QR Code *");
        return ResponseEntity.ok(this.qrcodeClientService.generateNapasQRCode(DTO)).getBody();
    }

    @PostMapping("/napas/check")
    public ResponseEntity<GlobalReponse > checkNapasQRCode(@RequestBody @Valid final CheckNapasQRCodeDto DTO) {
        log.info("*** QRCodeDto, resource; GENERATE NAPAS QR Code *");
        return ResponseEntity.ok(this.qrcodeClientService.checkNapasQRCode(DTO)).getBody();
    }
}
