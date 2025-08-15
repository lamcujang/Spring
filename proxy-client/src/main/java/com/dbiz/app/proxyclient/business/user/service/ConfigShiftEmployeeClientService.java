package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftEmployeeQueryRequest;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "configShiftEmployeeClientService", path = "/user-service/api/v1/configShiftEmployee", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ConfigShiftEmployeeClientService {

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ConfigShiftEmployeeQueryRequest request);
    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody ConfigShiftEmployeeDto request);
}
