package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.CheckQRCodeMBBDto;
import org.common.dbiz.dto.paymentDto.QRCodeDto;
import org.common.dbiz.dto.paymentDto.QRCodeMBBDto;
import org.common.dbiz.dto.paymentDto.napas.CheckNapasQRCodeDto;
import org.common.dbiz.dto.paymentDto.request.QRCodeReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "qrcodeClientService", path = "/payment-service/api/v1/qrcode")
public interface QRCodeClientService {

    @PostMapping("/generateMBB")
    public ResponseEntity<GlobalReponse > generateQRCodeMBB(@RequestBody @Valid final QRCodeMBBDto DTO);

    @PostMapping("/checkOrderMBB")
    public ResponseEntity<GlobalReponse > checkQRCodeMBB(@RequestBody @Valid final CheckQRCodeMBBDto DTO);

    @PostMapping("/generate")
    public ResponseEntity<GlobalReponse > generateQRCode(@RequestBody @Valid final QRCodeDto DTO);

    @PostMapping("/generateByBankAccount")
    public ResponseEntity<GlobalReponse > generateQRCodeByBank(@RequestBody @Valid final QRCodeDto DTO);

    @PostMapping("/napas/generate")
    public ResponseEntity<GlobalReponse > generateNapasQRCode(@RequestBody @Valid final QRCodeReqDto DTO);

    @PostMapping("/napas/check")
    public ResponseEntity<GlobalReponse > checkNapasQRCode(@RequestBody @Valid final CheckNapasQRCodeDto DTO);
}
