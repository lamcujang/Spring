package com.dbiz.app.tenantservice.service;

import org.common.dbiz.request.tenantRequest.AuditLogRequest;

public interface AuditLogProducer {

    void sendAuditLog(AuditLogRequest auditLogRequest);
}
