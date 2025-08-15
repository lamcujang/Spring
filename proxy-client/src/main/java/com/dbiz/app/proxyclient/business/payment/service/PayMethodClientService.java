package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.PayMethodAndOrgDto;
import org.common.dbiz.dto.paymentDto.PayMethodDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.PayMethodQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "PayMethodClientService", path = "/payment-service/api/v1/payMethod")
public interface PayMethodClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap final PayMethodQueryRequest  request);

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final PayMethodDto  dto);
    @PostMapping("/savePayMethod")
    public ResponseEntity<GlobalReponse> savePayMethod(@RequestBody @Valid final PayMethodAndOrgDto dto);
}
