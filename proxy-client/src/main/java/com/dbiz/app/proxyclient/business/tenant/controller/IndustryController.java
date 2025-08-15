package com.dbiz.app.proxyclient.business.tenant.controller;

import com.dbiz.app.proxyclient.business.tenant.service.IndustryClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.IndustryQueryRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/industry")
@RequiredArgsConstructor
public class IndustryController {
    private final IndustryClientService industryClientService;

    @GetMapping("/getAll")
    public ResponseEntity<GlobalReponsePagination> findAllIndustry(@SpringQueryMap IndustryQueryRequest request) {
        GlobalReponsePagination response = this.industryClientService.findAllIndustry(request).getBody();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllGroupByType")
    public ResponseEntity<GlobalReponsePagination> getAllGroupByType() {
        GlobalReponsePagination response = this.industryClientService.getAllGroupByType().getBody();
        return ResponseEntity.ok(response);
    }
}
