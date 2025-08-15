package com.dbiz.app.reportservice.resource;

import com.dbiz.app.reportservice.service.ExpenseTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.reportDto.request.ExpenseTypeRequest;
import org.common.dbiz.dto.reportDto.request.ReportReqDto;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenseTypes")
@Slf4j
@RequiredArgsConstructor
public class ExpenseTypeResource {
    private final ExpenseTypeService expenseTypeService;

    @GetMapping
    public ResponseEntity<GlobalReponsePagination> getAll(@ModelAttribute ExpenseTypeRequest req) {
        log.info("*** Note, controller; fetch Expense Type*");
        return ResponseEntity.ok(this.expenseTypeService.findAll(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id) {
        log.info("*** ExpenseTypeDto, controller; fetch ExpenseType by id *");
        return ResponseEntity.ok(this.expenseTypeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GlobalReponse> save(@RequestBody ExpenseTypeDto dto) {
        log.info("*** Note, controller; save Expense Type *");
        return ResponseEntity.ok(this.expenseTypeService.save(dto));
    }
}
