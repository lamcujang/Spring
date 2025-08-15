package com.dbiz.app.proxyclient.business.inventory.controller;


import com.dbiz.app.proxyclient.business.inventory.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.inventoryDto.TransactionParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAllTransaction(@SpringQueryMap TransactionParamDto dto){
        log.info("*** Transaction, resource; Find All Transaction ***");
        return ResponseEntity.ok(this.transactionService.findAllTransaction(dto)).getBody();
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalReponse> createTransaction(@RequestBody @Valid final TransactionDto DTO) {
        log.info("*** TransactionDto, resource; create TransactionDto *");
        return ResponseEntity.ok(this.transactionService.createTransaction(DTO)).getBody();
    }
}
