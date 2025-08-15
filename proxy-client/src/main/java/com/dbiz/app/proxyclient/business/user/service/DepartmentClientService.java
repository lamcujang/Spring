package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DepartmentQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "departmentClientService", path = "/user-service/api/v1/departments", decode404 = true
        , configuration = FeignClientConfig.class )
public interface DepartmentClientService {

    @GetMapping("/findAll")
    GlobalReponsePagination findAll(@SpringQueryMap DepartmentQueryRequest request);

    @PostMapping("/save")
    GlobalReponse save(@RequestBody DepartmentDto dto);
}
