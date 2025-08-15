package com.dbiz.app.paymentservice.resource;


import org.common.dbiz.dto.paymentDto.PayMethodAndOrgDto;
import org.common.dbiz.dto.paymentDto.PayMethodDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import com.dbiz.app.paymentservice.service.PayMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payMethod")
@Slf4j
@RequiredArgsConstructor
public class PayMethodResource {

    private final PayMethodService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final PayMethodQueryRequest request) {
        log.info("*** Paymethod, resource; find all paymethods  ***");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final PayMethodDto dto) {
        log.info("*** Paymethod, resource; save paymethod  ***");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @PostMapping("/savePayMethod")
    public ResponseEntity<GlobalReponse> savePayMethod(@RequestBody @Valid final PayMethodAndOrgDto dto) {
        log.info("*** Paymethod, resource; save paymethod  ***");
        return ResponseEntity.ok(this.service.savePayMethod(dto));
    }
}
