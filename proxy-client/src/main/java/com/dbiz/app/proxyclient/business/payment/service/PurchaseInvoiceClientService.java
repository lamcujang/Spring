package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.PurchaseInvoiceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.orderRequest.POHeaderVRequest;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import org.common.dbiz.request.paymentRequest.PurchaseInvoiceQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "PurchaseInvoiceClientService", path = "/payment-service/api/v1/purchaseInvoices")
public interface PurchaseInvoiceClientService {
    @GetMapping("/po")
    public ResponseEntity<GlobalReponsePagination> getPO(@SpringQueryMap final POHeaderVRequest req);

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody final PurchaseInvoiceDto req) ;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final PurchaseInvoiceQueryRequest req);

    @GetMapping("{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id);
}
