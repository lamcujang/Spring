package com.dbiz.app.proxyclient.business.tenant.service;

import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.IndustryQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "TENANT-SERVICE", contextId = "industryClientService", path = "/tenant-service/api/v1/industry", decode404 = true)
public interface IndustryClientService {
    @GetMapping("/getAll")
    public ResponseEntity<GlobalReponsePagination> findAllIndustry(@SpringQueryMap IndustryQueryRequest request);

    @GetMapping("/getAllGroupByType")
    public ResponseEntity<GlobalReponsePagination> getAllGroupByType();
}
