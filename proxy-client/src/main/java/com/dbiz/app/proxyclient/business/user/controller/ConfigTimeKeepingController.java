package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.ConfigTimeKeepingClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigTimeKeepingQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configTimeKeeping")
@Slf4j
@RequiredArgsConstructor
public class ConfigTimeKeepingController {
    private final ConfigTimeKeepingClientService configTimeKeepingClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ConfigTimeKeepingQueryRequest request){
        log.info("*** ConfigTimeKeeping List, resource; fetch all config time keepings ***");
        return ResponseEntity.ok(this.configTimeKeepingClientService.findAll(request)).getBody();
    }

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody ConfigTimeKeepingDto request){
        log.info("*** ConfigTimeKeeping, resource; save config time keeping ***");
        return ResponseEntity.ok(this.configTimeKeepingClientService.save(request)).getBody();
    }
}
