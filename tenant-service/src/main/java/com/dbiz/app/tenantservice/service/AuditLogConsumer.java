package com.dbiz.app.tenantservice.service;

import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.kafka.support.Acknowledgment;

public interface AuditLogConsumer {
    void consumeAuditLog(AuditLogRequest auditLogRequest, Acknowledgment ack);
}
