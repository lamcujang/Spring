package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.order.service.NapasOrderService;
import com.dbiz.app.proxyclient.business.payment.service.NapasPaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.dto.paymentDto.napas.ReconciliationReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/napas")
@Slf4j
@RequiredArgsConstructor
public class NapasController {


    private final NapasOrderService service;
    private final NapasPaymentService napasPaymentService;
    private final ObjectMapper objectMapper;

    @PostMapping("/notification")
    public ResponseEntity<NapasResponseDto> updateNapasOrder(@RequestBody @Valid final PayloadNapasDto DTO) {

        log.info("*** OrderDto, resource; save order *");
        try {
            log.info("payload: " + objectMapper.writeValueAsString(DTO));
        }catch (JsonProcessingException e) {
            log.error("Error converting DTO to JSON", e);
        }
        return ResponseEntity.ok(this.service.updateNapasOrder(DTO)).getBody();
    }

    @PostMapping("/investigation")
    public ResponseEntity<NapasResponseDto> updateNapasOrder2(@RequestBody @Valid final PayloadNapasDto DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.service.updateNapasOrder2(DTO)).getBody();
    }

    @PostMapping("/internal/transaction")
    public ResponseEntity<GlobalReponse> createNapasTransaction(@RequestBody @Valid final NotificationDto DTO) {
        log.info("createNapasTransaction: {}", DTO);
        return ResponseEntity.ok(this.napasPaymentService.createNapasTransaction(DTO)).getBody();
    }

    @PostMapping("/reconciliation")
    public NapasResponseDto createNapasReconciliation(@RequestBody @Valid final PayloadNapasDto DTO) {
        log.info("createNapasReconciliation: {}", DTO);
        return ResponseEntity.ok(this.napasPaymentService.createNapasReconciliation(DTO)).getBody();
    }
}
