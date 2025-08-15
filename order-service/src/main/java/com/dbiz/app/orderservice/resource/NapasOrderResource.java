package com.dbiz.app.orderservice.resource;


import com.dbiz.app.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.OrderDto;
import org.common.dbiz.dto.orderDto.PaymentDto;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.dto.paymentDto.napas.NotificationDto;
import org.common.dbiz.dto.paymentDto.napas.PayloadNapasDto;
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
public class NapasOrderResource {


    private final OrderService orderService;

    @PostMapping("/notification")
    public ResponseEntity<NapasResponseDto> updateNapasOrder(@RequestBody @Valid final PayloadNapasDto DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.orderService.updateNapasOrder(DTO));
    }


    @PostMapping("/investigation")
    public ResponseEntity<NapasResponseDto> updateNapasOrder2(@RequestBody @Valid final PayloadNapasDto DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.orderService.updateNapasOrder(DTO));
    }

}
