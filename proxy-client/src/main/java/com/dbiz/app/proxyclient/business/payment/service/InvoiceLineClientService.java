package com.dbiz.app.proxyclient.business.payment.service;


import org.common.dbiz.dto.paymentDto.InvoiceLineListDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.InvoiceLineQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "invoiceLineClientService", path = "/payment-service/api/v1/invoiceLines")
public interface InvoiceLineClientService {

    @GetMapping
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final InvoiceLineQueryRequest  entityQueryRequest);


    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final InvoiceLineListDto  DTO);
}
