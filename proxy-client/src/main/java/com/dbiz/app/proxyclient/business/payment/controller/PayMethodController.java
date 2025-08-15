package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.PayMethodClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PayMethodAndOrgDto;
import org.common.dbiz.dto.paymentDto.PayMethodDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payMethod")
@Slf4j
@RequiredArgsConstructor
public class PayMethodController {

    private final PayMethodClientService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap @Valid final PayMethodQueryRequest request)
    {
        log.info("*** PayMethod, Controller; fetch all PayMethod  ***");
        return ResponseEntity.ok(this.service.findAll(request).getBody());

    };

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final PayMethodDto request)
    {
        log.info("*** PayMethod, Controller; fetch all PayMethod  ***");
        return ResponseEntity.ok(this.service.save(request).getBody());

    };
    @PostMapping("/savePayMethod")
    public ResponseEntity<GlobalReponse> savePayMethod(@RequestBody @Valid final PayMethodAndOrgDto dto) {
        log.info("*** Paymethod, resource; save paymethod  ***");
        return ResponseEntity.ok(this.service.savePayMethod(dto).getBody());
    }
}
