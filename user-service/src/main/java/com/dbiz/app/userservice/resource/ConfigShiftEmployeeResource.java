package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.ConfigShiftEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftEmployeeQueryRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/api/v1/configShiftEmployee"})
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftEmployeeResource {
    private final ConfigShiftEmployeeService configShiftEmployeeService;

    @GetMapping("/findAll")
    public GlobalReponsePagination findAll(@ModelAttribute ConfigShiftEmployeeQueryRequest request) {
        log.info("*** ConfigShiftEmployee List, resource; fetch all config shift employees ***");
        return this.configShiftEmployeeService.findAll(request);
    }

    @PostMapping("/save")
    public GlobalReponse save(@RequestBody ConfigShiftEmployeeDto request) {
        log.info("*** ConfigShiftEmployee, resource; save config shift employee ***");
        return this.configShiftEmployeeService.save(request);
    }

}
