package com.dbiz.app.paymentservice.resource;


import com.dbiz.app.paymentservice.service.BankAccountService;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.request.BankAccountReqDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.paymentRequest.BankAccountQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/bankAccounts")
@Slf4j
@RequiredArgsConstructor
public class BankAccountResource {

    private final BankAccountService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination > findAll(@ModelAttribute @Valid final BankAccountQueryRequest request) {
        log.info("*** BankAccount, resource; generate BankAccount  ***");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @GetMapping("/findByBank")
    public ResponseEntity<GlobalReponsePagination> findByBank(@ModelAttribute @Valid final BankAccountReqDto reqDto) {
        log.info("*** BankAccount, resource; generate BankAccount by BankId ***");
        return ResponseEntity.ok(this.service.findByBank(reqDto));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse > save(@RequestBody @Valid final BankAccountDto dto) {
        log.info("*** BankAccount, resource; save BankAccount ***");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @PostMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody @Valid final BankAccountDto dto) {
        log.info("*** BankAccount, resource; update BankAccount ***");
        return ResponseEntity.ok(this.service.save(dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> deleteById(@PathVariable final Integer id) {
        log.info("*** BankAccount, resource; delete BankAccount by id ***");
        return ResponseEntity.ok(this.service.deleteById(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable final Integer id) {
        log.info("*** BankAccount, resource; fetch BankAccount by id ***");
        return ResponseEntity.ok(this.service.findById(id));
    }
}
