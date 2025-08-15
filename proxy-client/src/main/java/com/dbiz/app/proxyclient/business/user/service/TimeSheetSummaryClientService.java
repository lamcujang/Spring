package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.TimeSheetSummaryDto;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "TimeSheetSummaryClientService", path = "/user-service/api/v1/timesheetSummaries"
        , configuration = FeignClientConfig.class
)
public interface TimeSheetSummaryClientService {
    @GetMapping
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap TimeSheetSummaryRequest request);

    @PostMapping
    ResponseEntity<GlobalReponse>  save(@RequestBody TimeSheetSummaryDto request);

    @GetMapping("/detail")
    ResponseEntity<GlobalReponse> getTimeSheetDetail(@SpringQueryMap TimeSheetSummaryRequest request);
}
