package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.SalaryConfigDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.common.dbiz.request.userRequest.SalaryConfigQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "salaryConfigClientService", path = "/user-service/api/v1/salaryConfig"
        , configuration = FeignClientConfig.class
)
public interface SalaryConfigClientService {

        @GetMapping("/findAll")
        ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap SalaryConfigQueryRequest request);
        @PostMapping("/save")
        ResponseEntity<GlobalReponse> save(@RequestBody SalaryConfigDto request);
    }
