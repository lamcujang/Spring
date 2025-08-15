package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.napas.NpIntRequestDto;
import org.common.dbiz.dto.paymentDto.request.BankNpReqDto;
import org.common.dbiz.dto.paymentDto.request.NpIntRequestReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "NpIntRequestClientService", path = "/payment-service/api/v1/napas/int")
public interface NpIntRequestClientService {

    @GetMapping("/bank")
    ResponseEntity<GlobalReponsePagination> findBank(@SpringQueryMap @Valid final BankNpReqDto reqDto);

    @GetMapping("/request")
    ResponseEntity<GlobalReponsePagination> findRequest(@SpringQueryMap @Valid final NpIntRequestReqDto request);

    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody @Valid final NpIntRequestDto dto);

}
