package com.dbiz.app.paymentservice.resource;


import com.dbiz.app.paymentservice.service.NapasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.common.dbiz.dto.paymentDto.napas.ReconciliationReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/napas")
@Slf4j
@RequiredArgsConstructor
public class NapasResource {

    private final NapasService napasService;

    @PostMapping("/internal/transaction")
    public GlobalReponse createNapasTransaction(@RequestBody @Valid final NotificationDto dto) {
        log.info("createNapasTransaction: {}", dto);
        return napasService.createNapasTransaction(dto);
    }

    @PostMapping("/reconciliation")
    public NapasResponseDto createNapasReconciliation(@RequestBody @Valid final PayloadNapasDto dto) {
        log.info("createNapasReconciliation: {}", dto);
        return napasService.createNapasReconciliation(dto);
    }
}
