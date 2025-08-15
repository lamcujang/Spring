package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.DepartmentClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DepartmentQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {
    private final DepartmentClientService departmentClientService;


    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap DepartmentQueryRequest request)
    {
        log.info("*** Department List, resource; fetch all departments ***");
        return this.departmentClientService.findAll(request);
    }

    @PostMapping("/save")
    GlobalReponse save(@RequestBody DepartmentDto dto){
        log.info("*** Department, resource; save department ***");
        return this.departmentClientService.save(dto);
    }
}
