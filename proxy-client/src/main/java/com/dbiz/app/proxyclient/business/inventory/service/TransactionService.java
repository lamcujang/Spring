package com.dbiz.app.proxyclient.business.inventory.service;


import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.inventoryDto.TransactionParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "INVENTORY-SERVICE", contextId = "transactionService",
        path = "/inventory-service/api/v1/transaction")
public interface TransactionService {

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAllTransaction(@SpringQueryMap TransactionParamDto dto);

    @PostMapping("/create")
    public ResponseEntity<GlobalReponse> createTransaction(@RequestBody @Valid final TransactionDto DTO);
}
