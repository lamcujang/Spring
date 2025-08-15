package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.NpIntRequestClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NpIntRequestDto;
import org.common.dbiz.dto.paymentDto.request.BankNpReqDto;
import org.common.dbiz.dto.paymentDto.request.NpIntRequestReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/napas/int")
@Slf4j
@RequiredArgsConstructor
public class NpIntRequestController {

    private final NpIntRequestClientService service;

    @GetMapping("/bank")
    public ResponseEntity<GlobalReponsePagination> findBank(@ModelAttribute @Valid final BankNpReqDto reqDto) {
        log.info("*** NapasInt resource; find BankNpStatus ***");
        return ResponseEntity.ok(this.service.findBank(reqDto).getBody());
    }

    @GetMapping("/request")
    public ResponseEntity<GlobalReponsePagination> findRequest(@ModelAttribute @Valid final NpIntRequestReqDto request)
    {
        log.info("*** NapasInt, resource; find NpIntRequest ***");
        return ResponseEntity.ok(this.service.findRequest(request).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final NpIntRequestDto dto) {
        log.info("*** NapasInt, resource; save NpIntRequest ***");
        return ResponseEntity.ok(this.service.save(dto).getBody());
    }

}
