package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.ConfigSalaryBonusAllowanceDto;
import org.common.dbiz.payload.GlobalReponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE", contextId = "configSalaryBonusAllowanceService", path = "/user-service/api/v1/configSalaryBonusAllowance", decode404 = true
        , configuration = FeignClientConfig.class )
public interface ConfigSalaryBonusAllowanceClientService {
    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody ConfigSalaryBonusAllowanceDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);
}
