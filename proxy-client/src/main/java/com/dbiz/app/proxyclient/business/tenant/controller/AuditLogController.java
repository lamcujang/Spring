package com.dbiz.app.proxyclient.business.tenant.controller;

import com.dbiz.app.proxyclient.business.tenant.service.AuditLogClientService;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit-log")
@RequiredArgsConstructor
public class AuditLogController {
    private static final Logger log = LoggerFactory.getLogger(AuditLogController.class);
    private final AuditLogClientService auditLogClientService;

    @GetMapping("/findAll")
    public ResponseEntity<GlobalReponsePagination> getAllAuditLogs(@SpringQueryMap AuditLogRequest auditLogRequest) {
        log.info("AuditLogController: getAllAuditLogs called with request: {}", auditLogRequest);
        return ResponseEntity.ok(auditLogClientService.getAllAuditLogs(auditLogRequest).getBody());
    }
}