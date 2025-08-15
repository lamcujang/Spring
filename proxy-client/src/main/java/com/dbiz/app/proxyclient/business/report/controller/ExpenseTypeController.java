package com.dbiz.app.proxyclient.business.report.controller;

import com.dbiz.app.proxyclient.business.report.service.ExpenseTypeClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.reportDto.request.ExpenseTypeRequest;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenseTypes")
@RequiredArgsConstructor
public class ExpenseTypeController {
    private final ExpenseTypeClientService clientService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap ExpenseTypeRequest req) {
        return ResponseEntity.ok(this.clientService.getAll(req)).getBody();
    }

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
        return ResponseEntity.ok(this.clientService.findById(id)).getBody();
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody ExpenseTypeDto dto) {
        return ResponseEntity.ok(this.clientService.save(dto)).getBody();
    }
}
