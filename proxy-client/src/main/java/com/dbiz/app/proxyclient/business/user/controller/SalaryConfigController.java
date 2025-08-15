package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.SalaryConfigClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.userDto.SalaryConfigDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.SalaryConfigQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/salaryConfig")
@RequiredArgsConstructor
public class SalaryConfigController {
    private final SalaryConfigClientService salaryConfigClientService;

    private final ObjectMapper mapper ;
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap SalaryConfigQueryRequest request){
        return salaryConfigClientService.findAll(request);
    }
    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody SalaryConfigDto request){

        return salaryConfigClientService.save(request);
    }
}
