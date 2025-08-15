package com.dbiz.app.proxyclient.business.user.controller;

import com.dbiz.app.proxyclient.business.user.service.TimeKeepingClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.dto.userDto.TimeKeepingDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.TimeKeepingQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/timeKeeping")
@RequiredArgsConstructor
public class TimeKeepingController {

    private final TimeKeepingClientService timeKeepingClientService;

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap TimeKeepingQueryRequest request) {
        return ResponseEntity.ok(timeKeepingClientService.findAll(request)).getBody();
    }


    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody TimeKeepingDto request) {
        return ResponseEntity.ok(timeKeepingClientService.save(request)).getBody();
    }
}
