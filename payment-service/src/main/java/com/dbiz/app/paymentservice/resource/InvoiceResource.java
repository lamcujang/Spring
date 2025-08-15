package com.dbiz.app.paymentservice.resource;


import org.common.dbiz.dto.paymentDto.InvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import com.dbiz.app.paymentservice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/invoices")
@Slf4j
@RequiredArgsConstructor
public class InvoiceResource {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute final InvoiceQueryRequest entityQueryRequest) {
        log.info("*** Invoice, controller; fetch Invoices all *");
        return ResponseEntity.ok(this.invoiceService.findAll(entityQueryRequest));
    }


    @PostMapping
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final InvoiceDto DTO) {
        log.info("*** InvoiceDto, resource; save InvoiceDto *");
        return ResponseEntity.ok(this.invoiceService.save(DTO));
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<GlobalReponse > findById(@PathVariable final Integer id) {
        log.info("*** InvoiceDto, resource; fetch InvoiceDto by id *");
        return ResponseEntity.ok(this.invoiceService.findById(id));
    }




}
