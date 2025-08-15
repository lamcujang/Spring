package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.PayMethodClientService;
import com.dbiz.app.proxyclient.business.payment.service.PurchaseInvoiceClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.PurchaseInvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import org.common.dbiz.request.paymentRequest.PurchaseInvoiceQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/purchaseInvoices")
@Slf4j
@RequiredArgsConstructor
public class PurchaseInvoiceControler {
    private final PurchaseInvoiceClientService service;

    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap @Valid final POHeaderVRequest req)
    {
        log.info("*** PayMethod, Controller; get purchase Orders  ***");
        return ResponseEntity.ok(this.service.getPO(req).getBody());
    };

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody final PurchaseInvoiceDto req) {
        log.info("*** PayMethod, Controller; save purchase invoice   ***");
        return ResponseEntity.ok(this.service.save(req).getBody());
    }

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final PurchaseInvoiceQueryRequest req){
        log.info("*** PayMethod, Controller; get purchase invoice   ***");
        return ResponseEntity.ok(this.service.findAll(req).getBody());
    }

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id){
        log.info("*** PayMethod, Controller; find purchase invoice By Id   ***");
        return ResponseEntity.ok(this.service.findById(id).getBody());
    }
}
