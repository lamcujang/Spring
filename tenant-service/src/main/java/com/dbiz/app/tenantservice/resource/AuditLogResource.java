package com.dbiz.app.tenantservice.resource;

import com.dbiz.app.tenantservice.repository.AuditLogRepository;
import com.dbiz.app.tenantservice.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit-log")
@RequiredArgsConstructor
@Slf4j
public class AuditLogResource {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogService auditLogService;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    @GetMapping("/findAll")
        public GlobalReponsePagination getAllAuditLogs(@ModelAttribute AuditLogRequest auditLogRequest) {
        log.info("Received request to fetch audit logs: {}", auditLogRequest);
        return auditLogService.getAllAuditLog(auditLogRequest);
    }
}
