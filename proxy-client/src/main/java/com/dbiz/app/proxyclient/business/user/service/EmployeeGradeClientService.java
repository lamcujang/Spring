package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "USER-SERVICE", contextId = "employeeGradeClientService", path = "/user-service/api/v1/employeeGrades", decode404 = true
        , configuration = FeignClientConfig.class )
public interface EmployeeGradeClientService {

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap EmployeeGradeQueryRequest request);


    @PostMapping("/save")
    GlobalReponse save(@RequestBody EmployeeGradeDto dto);
}
