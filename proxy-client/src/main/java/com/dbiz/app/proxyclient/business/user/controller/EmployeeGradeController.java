package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.EmployeeGradeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employeeGrades")
@RequiredArgsConstructor
@Slf4j
public class EmployeeGradeController {
    private final EmployeeGradeClientService employeeGradeClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap EmployeeGradeQueryRequest request)
    {
        log.info("*** EmployeeGrade List, resource; fetch all employee grades ***");
        return ResponseEntity.ok(this.employeeGradeClientService.findAll(request)).getBody();
    }


    @PostMapping("/save")
    GlobalReponse save(@RequestBody EmployeeGradeDto dto){
        log.info("*** EmployeeGrade, resource; save employee grade ***");
        return this.employeeGradeClientService.save(dto);
    }
}
