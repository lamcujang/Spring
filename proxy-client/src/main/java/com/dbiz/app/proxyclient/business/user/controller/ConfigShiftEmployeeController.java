package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.ConfigShiftClientService;
import com.dbiz.app.proxyclient.business.user.service.ConfigShiftEmployeeClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftEmployeeQueryRequest;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configShiftEmployee")
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftEmployeeController {

    private final ConfigShiftEmployeeClientService  configShiftEmployeeClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ConfigShiftEmployeeQueryRequest  request){
        log.info("*** ConfigShift List, resource; fetch all config shifts ***");
        return ResponseEntity.ok(this.configShiftEmployeeClientService.findAll(request)).getBody();
    }

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@ModelAttribute ConfigShiftEmployeeDto  dto)
    {
        log.info("*** ConfigShift, resource; save config shift ***");
        return ResponseEntity.ok(this.configShiftEmployeeClientService.save(dto)).getBody();
    }
}
