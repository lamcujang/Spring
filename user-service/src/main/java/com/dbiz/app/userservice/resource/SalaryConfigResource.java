package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.SalaryConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.SalaryConfigDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.SalaryConfigQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/salaryConfig"})
@Slf4j
@RequiredArgsConstructor
public class SalaryConfigResource {

    private final SalaryConfigService service;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute SalaryConfigQueryRequest request) {
        log.info("*** SalaryConfig, resource; fetch all salary configurations ***");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody SalaryConfigDto request) {
        log.info("*** SalaryConfig, resource; save salary configuration ***");
        return ResponseEntity.ok(this.service.save(request));
    }
}
