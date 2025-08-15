package com.dbiz.app.proxyclient.business.system.service;

import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.VersionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "versionClientService", path = "/system-service/api/v1/version", decode404 = true)
public interface VersionClientService {
    @GetMapping("/check")
    GlobalReponsePagination getAll(@SpringQueryMap VersionRequest req);
}
