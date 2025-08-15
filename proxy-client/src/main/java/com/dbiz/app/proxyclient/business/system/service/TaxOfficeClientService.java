package com.dbiz.app.proxyclient.business.system.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.TaxOfficeQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "SYSTEM-SERVICE", contextId = "taxOfficeClientService", path = "/system-service/api/v1/taxOffice")
public interface TaxOfficeClientService {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<GlobalReponse> loadTaxOffice(
            @RequestPart(value = "taxOffice") MultipartFile taxOffice,
            @RequestPart(value = "taxRegion") MultipartFile taxRegion);

    @GetMapping
    ResponseEntity<GlobalReponsePagination> findAllTaxOffice(@SpringQueryMap final TaxOfficeQueryRequest request);

    @GetMapping("/findAll")
    ResponseEntity<GlobalReponsePagination> findAll(@SpringQueryMap final TaxOfficeQueryRequest request);

}
