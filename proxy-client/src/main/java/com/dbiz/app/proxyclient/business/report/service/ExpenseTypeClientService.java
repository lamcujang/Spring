package com.dbiz.app.proxyclient.business.report.service;

import org.common.dbiz.dto.reportDto.request.ExpenseTypeRequest;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "REPORT-SERVICE", contextId = "expenseTypeService", path = "/report-service/api/v1/expenseTypes")
public interface ExpenseTypeClientService {
    @GetMapping
    ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap ExpenseTypeRequest req);

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable(name = "id") Integer id);

    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody ExpenseTypeDto dto);
}
