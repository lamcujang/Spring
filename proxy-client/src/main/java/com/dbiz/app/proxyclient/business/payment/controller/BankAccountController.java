package com.dbiz.app.proxyclient.business.payment.controller;



import com.dbiz.app.proxyclient.business.payment.service.BankAccountClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.request.BankAccountReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/bankAccounts")
@Slf4j
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountClientService bankAccountClientService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@SpringQueryMap @Valid final BankAccountQueryRequest  request)
    {
        log.info("*** BankAccount, resource; generate BankAccount  ***");
        return ResponseEntity.ok(this.bankAccountClientService.findAll(request).getBody());

    };

    @GetMapping("/findByBank")
    public ResponseEntity<GlobalReponsePagination > findByBank(@SpringQueryMap @Valid final BankAccountReqDto reqDto) {
        log.info("*** BankAccount, resource; generate BankAccount by BankId ***");
        return ResponseEntity.ok(this.bankAccountClientService.findByBank(reqDto).getBody());
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody @Valid final BankAccountDto  dto){
        log.info("*** BankAccount, resource; save BankAccount ***");
        return ResponseEntity.ok(this.bankAccountClientService.save(dto).getBody());
    };

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final BankAccountDto dto)
    {
        log.info("*** BankAccount, resource; update BankAccount ***");
        return ResponseEntity.ok(this.bankAccountClientService.update(dto).getBody());
    };

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id){
        log.info("*** BankAccount, resource; delete BankAccount by id ***");
        return ResponseEntity.ok(this.bankAccountClientService.deleteById(id).getBody());
    };


    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id){
        log.info("*** BankAccount, resource; fetch BankAccount by id ***");
        return ResponseEntity.ok(this.bankAccountClientService.findById(id).getBody());
    };
}
