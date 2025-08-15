package com.dbiz.app.proxyclient.business.tenant.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TENANT-SERVICE", contextId = "auditLogClientService", path = "/tenant-service/api/v1/audit-log")
public interface AuditLogClientService {

	@GetMapping("/findAll")
	ResponseEntity<GlobalReponsePagination> getAllAuditLogs(@SpringQueryMap AuditLogRequest auditLogRequest);
}