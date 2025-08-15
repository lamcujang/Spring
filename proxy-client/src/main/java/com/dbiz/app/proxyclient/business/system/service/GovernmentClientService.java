package com.dbiz.app.proxyclient.business.system.service;

import com.dbiz.app.proxyclient.config.client.FeignClientConfig;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ProvinceQueryRequest;
import org.common.dbiz.request.systemRequest.WardQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "governmentClientService", path = "/system-service/api/v1/government")
public interface GovernmentClientService {

    @PostMapping
    ResponseEntity<GlobalReponse> loadAdministrativeDivisions();

    @GetMapping
    ResponseEntity<GlobalReponsePagination> getProvinceWithWard(@SpringQueryMap final ProvinceQueryRequest request);

    @GetMapping("/province")
    ResponseEntity<GlobalReponsePagination> getProvince(@SpringQueryMap final ProvinceQueryRequest request);

    @GetMapping("/ward")
    ResponseEntity<GlobalReponsePagination> getWard(@SpringQueryMap final WardQueryRequest request);

}
