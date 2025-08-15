package com.dbiz.app.paymentservice.resource;


import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import com.dbiz.app.paymentservice.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/banks")
@Slf4j
@RequiredArgsConstructor
public class BankResource {

    private final BankService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute @Valid final BankQueryRequest request) {
        log.info("*** Bank, resource; Find All Bank  ***");
        return ResponseEntity.ok(this.service.findAll(request));
    }
}
