package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.LeaveApplicationDto;
import org.common.dbiz.dto.userDto.request.LeaveApplicationRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE", contextId = "LeaveApplicationService", path = "/user-service/api/v1/leaveApplications"
        , configuration = FeignClientConfig.class
)
public interface LeaveApplicationClientService {
    @GetMapping
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap LeaveApplicationRequest request);

    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody LeaveApplicationDto request);

    @DeleteMapping("/{id}")
    ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);
}
