package com.dbiz.app.proxyclient.business.order.service;


import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "ORDER-SERVICE", contextId = "napasOrderService", path = "/order-service/api/v1/napas")

public interface NapasOrderService {

    @PostMapping("/notification")
    public ResponseEntity<NapasResponseDto> updateNapasOrder(@RequestBody @Valid final PayloadNapasDto DTO);

    @PostMapping("/investigation")
    public ResponseEntity<NapasResponseDto> updateNapasOrder2(@RequestBody @Valid final PayloadNapasDto DTO);
}
