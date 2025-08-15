package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.BankClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/banks")
@Slf4j
@RequiredArgsConstructor
public class BankController {

    private final BankClientService bankClientService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap @Valid final BankQueryRequest request)
    {
        log.info("*** Bank, Controller; fetch all bank  ***");
        return ResponseEntity.ok(this.bankClientService.findAll(request).getBody());

    };
}
