package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.EmployeeGradeService;
import com.dbiz.app.userservice.service.EmployeeService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeDto;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.common.dbiz.request.userRequest.EmployeeQueryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/employees"})
@Slf4j
@RequiredArgsConstructor
public class EmployeeResource {
    private final EmployeeService employeeService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute EmployeeQueryRequest request) {
        log.info("*** Employee List, resource; fetch all employees *");
        return this.employeeService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody EmployeeDto dto) {
        log.info("*** Employee, resource; save employee *");
        return this.employeeService.save(dto);
    }

}
