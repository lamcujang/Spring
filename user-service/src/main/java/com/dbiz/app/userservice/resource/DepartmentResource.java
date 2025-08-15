package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.dto.userDto.PartnerGroupDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DepartmentQueryRequest;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/departments"})
@Slf4j
@RequiredArgsConstructor
public class DepartmentResource {

    private final DepartmentService departmentService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute DepartmentQueryRequest request) {
        log.info("*** PartnerGroup List, resource; fetch all vendor *");

        return this.departmentService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody DepartmentDto dto) {
        log.info("*** DepartmentDto, resource; save department *");
        return this.departmentService.save(dto);
    }


}
