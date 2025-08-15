package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.EmployeeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final EmployeeClientService employeeClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap EmployeeQueryRequest request)
    {
        log.info("*** Employee List, resource; fetch all employees ***");
        return ResponseEntity.ok(this.employeeClientService.findAll(request)).getBody();
    }


    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody EmployeeDto dto){
        log.info("*** Employee, resource; save employee ***");
        return ResponseEntity.ok(this.employeeClientService.save(dto)).getBody();
    }
}
