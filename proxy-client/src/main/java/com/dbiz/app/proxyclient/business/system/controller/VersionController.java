package com.dbiz.app.proxyclient.business.system.controller;

import com.dbiz.app.proxyclient.business.system.service.VersionClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.systemRequest.ReferenceQueryRequest;
import org.common.dbiz.request.systemRequest.VersionRequest;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/version")
@RequiredArgsConstructor
public class VersionController {
    private final VersionClientService clientService;

    @GetMapping("/check")
    public GlobalReponsePagination findAll(@SpringQueryMap VersionRequest request) {
        return this.clientService.getAll(request);
    }
}
