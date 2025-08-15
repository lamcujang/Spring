package com.dbiz.app.proxyclient.business.payment.controller;


import com.dbiz.app.proxyclient.business.payment.service.InvoiceClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.InvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/invoices")
@Slf4j
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceClientService invoiceClientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final InvoiceQueryRequest entityQueryRequest) {
        log.info("*** Order, controller; fetch Order all *");
        return ResponseEntity.ok(this.invoiceClientService.findAll(entityQueryRequest).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final InvoiceDto DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.invoiceClientService.save(DTO).getBody());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** OrderDto, resource; fetch order by id *");
        return ResponseEntity.ok(this.invoiceClientService.findById(id).getBody());
    }
}
