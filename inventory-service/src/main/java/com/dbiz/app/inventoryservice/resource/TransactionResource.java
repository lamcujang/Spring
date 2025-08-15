package com.dbiz.app.inventoryservice.resource;


import com.dbiz.app.inventoryservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.inventoryDto.TransactionDto;
import org.common.dbiz.dto.inventoryDto.TransactionParamDto;
import org.common.dbiz.dto.orderDto.OrderLineListDto;
import org.common.dbiz.dto.paymentDto.coupon.CouponParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionResource {

    private final TransactionService transactionService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAllTransaction(@ModelAttribute TransactionParamDto dto){
        log.info("*** Transaction, resource; Find All Transaction ***");
        return ResponseEntity.ok(this.transactionService.findAllTransaction(dto));
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalReponse> createTransaction(@RequestBody @Valid final TransactionDto DTO) {
        log.info("*** TransactionDto, resource; create TransactionDto *");
        return ResponseEntity.ok(this.transactionService.createTransaction(DTO));
    }
}
