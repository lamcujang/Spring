package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.dto.paymentDto.napas.ReconciliationReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "napasPaymentService", path = "/payment-service/api/v1/napas")
public interface NapasPaymentService {


    @PostMapping("/internal/transaction")
    public ResponseEntity<GlobalReponse> createNapasTransaction(@RequestBody @Valid final NotificationDto DTO);


    @PostMapping("/reconciliation")
    public NapasResponseDto createNapasReconciliation(@RequestBody @Valid final PayloadNapasDto DTO);
}
