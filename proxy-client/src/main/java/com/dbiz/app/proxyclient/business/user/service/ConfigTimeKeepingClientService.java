package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigTimeKeepingQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "configTimeKeepingClientService", path = "/user-service/api/v1/configTimeKeeping", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ConfigTimeKeepingClientService {
    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap ConfigTimeKeepingQueryRequest request);

    @PostMapping("/save")
    ResponseEntity<GlobalReponse> save(@RequestBody ConfigTimeKeepingDto request);
}
