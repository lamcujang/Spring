package com.dbiz.app.proxyclient.business.tenant.controller;


import com.dbiz.app.proxyclient.business.tenant.service.PrintReportService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.tenantDto.PrintReportDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.PrintReportQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/printReports")
@RequiredArgsConstructor
public class PrintReportController {
    private final PrintReportService entityClientService;

    @GetMapping("/{id}")
    ResponseEntity<GlobalReponse> findById(@PathVariable("id") final Integer id){
        return ResponseEntity.ok(this.entityClientService.findById(id).getBody());
    };
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(
            @SpringQueryMap final PrintReportQueryRequest PrintReportQueryRequest
            ){
        return ResponseEntity.ok(this.entityClientService.findAll(PrintReportQueryRequest).getBody());
    }

    @GetMapping("/findAllByTenant")
    ResponseEntity<GlobalReponsePagination> findAllByTenant(
            @SpringQueryMap final PrintReportQueryRequest PrintReportQueryRequest
    ){
        return ResponseEntity.ok(this.entityClientService.findAllByTenant(PrintReportQueryRequest).getBody());
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody final PrintReportDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.save(entityDto).getBody());
    }

    @PostMapping("/saveAll")
    public ResponseEntity<GlobalReponse> saveAll(@RequestBody final List<Map<String, Object>> rawList) {
        return ResponseEntity.ok(this.entityClientService.saveAll(rawList).getBody());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalReponse> delete(@PathVariable("id") final Integer id) {
        return ResponseEntity.ok(this.entityClientService.deleteById(id).getBody());
    }

    @PutMapping("/update")
    public ResponseEntity<GlobalReponse> update(@RequestBody final PrintReportDto entityDto) {
        return ResponseEntity.ok(this.entityClientService.update(entityDto).getBody());
    }
}
