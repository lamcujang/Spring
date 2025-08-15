package com.dbiz.app.proxyclient.business.payment.service;


import org.common.dbiz.dto.orderDto.InvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "invoiceClientService", path = "/payment-service/api/v1/invoices")
public interface InvoiceClientService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final InvoiceQueryRequest  entityQueryRequest);


    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final InvoiceDto  DTO);

    @GetMapping("/findById/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id);
}
