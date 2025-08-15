package com.dbiz.app.proxyclient.business.payment.controller;

import com.dbiz.app.proxyclient.business.payment.service.InvoiceLineClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.InvoiceLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceLineQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/invoiceLines")
@Slf4j
@RequiredArgsConstructor
public class InvoiceLineController {

    private final InvoiceLineClientService invoiceLineClientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final InvoiceLineQueryRequest entityQueryRequest) {
        log.info("*** Order, controller; fetch Order all *");
        return ResponseEntity.ok(this.invoiceLineClientService.findAll(entityQueryRequest).getBody());
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final InvoiceLineListDto DTO) {
        log.info("*** OrderDto, resource; save order *");
        return ResponseEntity.ok(this.invoiceLineClientService.save(DTO).getBody());
    }
}
