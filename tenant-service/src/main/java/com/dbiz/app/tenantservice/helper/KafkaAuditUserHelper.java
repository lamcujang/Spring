package com.dbiz.app.tenantservice.helper;

import com.dbiz.app.tenantservice.service.AuditLogProducer;
import lombok.RequiredArgsConstructor;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class KafkaAuditUserHelper {

    private final AuditLogProducer auditLogProducer;

    public void sendKafkaSaveAuditUser(
            Integer tenantId, // mainTenantId
            Integer orgId,
            String serviceName,
            String entityName,
            Integer entityId,
            String action,
            Integer userId,
            String description
    ) {

        AuditLogRequest auditLogRequest = AuditLogRequest.builder()
                .isForUser("Y")
                .tenantId(tenantId)
                .orgId(orgId)
                .serviceName(serviceName)
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .userId(userId)
                .timestamp(DateHelper.toInstantNowUTC().toString())
                .description(description)
                .build();

        auditLogProducer.sendAuditLog(auditLogRequest);

    }

}
