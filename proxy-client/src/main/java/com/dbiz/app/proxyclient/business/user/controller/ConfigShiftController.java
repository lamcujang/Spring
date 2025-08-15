package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.ConfigShiftClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configShift")
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftController {

    private final ConfigShiftClientService configShiftClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ConfigShiftQueryRequest request){
        log.info("*** ConfigShift List, resource; fetch all config shifts ***");
        return ResponseEntity.ok(this.configShiftClientService.findAll(request)).getBody();
    }

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody ConfigShiftDto dto)
    {
        log.info("*** ConfigShift, resource; save config shift ***");
        return ResponseEntity.ok(this.configShiftClientService.save(dto)).getBody();
    }
}
