package com.dbiz.app.proxyclient.business.payment.service;

import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.request.BankAccountReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "PAYMENT-SERVICE", contextId = "bankAccountClientService", path = "/payment-service/api/v1/bankAccounts")
public interface BankAccountClientService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap @Valid final BankAccountQueryRequest  request) ;

    @GetMapping("/findByBank")
    public ResponseEntity<GlobalReponsePagination> findByBank(@SpringQueryMap @Valid final BankAccountReqDto reqDto);

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final BankAccountDto  dto);

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final BankAccountDto dto);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id);


    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id);
}
