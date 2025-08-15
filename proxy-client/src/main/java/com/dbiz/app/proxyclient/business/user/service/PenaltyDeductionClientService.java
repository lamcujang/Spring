package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "PenaltyDeductionService", path = "/user-service/api/v1/penaltyDeductions"
        , configuration = FeignClientConfig.class
)
public interface PenaltyDeductionClientService {
    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody PenaltyDeductionDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);
}
