package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.dto.userDto.EmployeeBonusAllowancesDto;
import org.common.dbiz.dto.userDto.request.EmployeeBonusAllowancesRequest;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICE", contextId = "EmployeeBonusAllowancesService", path = "/user-service/api/v1/employeeBonusAllowances"
        , configuration = FeignClientConfig.class
)
public interface EmployeeBonusAllowancesClientService {
    @GetMapping
    ResponseEntity<GlobalReponsePagination> getAll(@SpringQueryMap EmployeeBonusAllowancesRequest req);

    @PostMapping
    ResponseEntity<GlobalReponse> save(@RequestBody EmployeeBonusAllowancesDto dto);

    @DeleteMapping("/{id}")
    ResponseEntity<GlobalReponse> delete(@PathVariable Integer id);
}
