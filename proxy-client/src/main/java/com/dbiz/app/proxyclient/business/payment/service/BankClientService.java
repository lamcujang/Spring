package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "banksClientService", path = "/payment-service/api/v1/banks")
public interface BankClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap @Valid final BankQueryRequest  request) ;
}
