package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "USER-SERVICE", contextId = "employeeContactClientService", path = "/user-service/api/v1/employeeGrades", decode404 = true
        , configuration = FeignClientConfig.class )
public interface EmployeeContactService {
}
