package com.dbiz.app.tenantservice.service;

import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;

public interface AuditLogService {
    GlobalReponsePagination getAllAuditLog(AuditLogRequest auditLogRequest);
}
