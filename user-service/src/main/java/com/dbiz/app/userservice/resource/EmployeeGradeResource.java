package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.EmployeeGradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/employeeGrades"})
@Slf4j
@RequiredArgsConstructor
public class EmployeeGradeResource {
    private final EmployeeGradeService employeeGradeService;


    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute EmployeeGradeQueryRequest request) {
        log.info("*** EmployeeGrade List, resource; fetch all employee grades *");

        return this.employeeGradeService.findAll(request);
    }


    @PostMapping("/save")
    public GlobalReponse save(@RequestBody EmployeeGradeDto dto) {
        log.info("*** EmployeeGrade, resource; save employee grade *");

        return this.employeeGradeService.save(dto);
    }
}
