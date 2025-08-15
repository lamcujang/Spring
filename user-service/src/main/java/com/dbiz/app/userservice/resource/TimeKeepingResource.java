package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.TimeKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.SalaryConfigDto;
import org.common.dbiz.dto.userDto.TimeKeepingDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.SalaryConfigQueryRequest;
import org.common.dbiz.request.userRequest.TimeKeepingQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/timeKeeping"})
@Slf4j
@RequiredArgsConstructor
public class TimeKeepingResource {
    private final TimeKeepingService service;


    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> findAll(@ModelAttribute TimeKeepingQueryRequest request) {
        log.info("*** SalaryConfig, resource; fetch all salary configurations ***");
        return ResponseEntity.ok(this.service.findAll(request));
    }

    @PostMapping("/save")
    public ResponseEntity<GlobalReponse> save(@RequestBody TimeKeepingDto request) {
        log.info("*** SalaryConfig, resource; save salary configuration ***");
        return ResponseEntity.ok(this.service.save(request));
    }
}
