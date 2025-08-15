package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.OverTimeLogDto;
import org.common.dbiz.dto.userDto.request.OverTimeLogRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE", contextId = "OvertimeLogService", path = "/user-service/api/v1/overTimeLogs"
        , configuration = FeignClientConfig.class
)
public interface OverTimeLogClientService {
    @GetMapping
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap OverTimeLogRequest request);

    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody OverTimeLogDto request);

    @DeleteMapping("/{id}")
    ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);
}
