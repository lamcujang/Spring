package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.TimeKeepingDto;
import org.common.dbiz.dto.userDto.TimeSheetSummaryDto;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.TimeKeepingQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "timeKeepingConfigClientService", path = "/user-service/api/v1/timeKeeping"
        , configuration = FeignClientConfig.class
)
public interface TimeKeepingClientService  {


    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap TimeKeepingQueryRequest request);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse>  save(@RequestBody TimeKeepingDto request);
}
