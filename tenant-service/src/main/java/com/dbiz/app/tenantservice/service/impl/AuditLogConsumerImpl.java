package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.domain.AbstractAuditLog;
import com.dbiz.app.tenantservice.domain.AuditLog;
import com.dbiz.app.tenantservice.domain.AuditLogUser;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.repository.AuditLogRepository;
import com.dbiz.app.tenantservice.repository.AuditLogUserRepository;
import com.dbiz.app.tenantservice.service.AuditLogConsumer;
import com.dbiz.app.tenantservice.service.data_source.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogConsumerImpl implements AuditLogConsumer {
    private final AuditLogRepository auditLogRepository;
    private final AuditLogUserRepository auditLogUserRepository;
    private final KafkaTemplate<String, AuditLogRequest> kafkaTemplate;
    private final DataSourceContextHolder dataSourceContextHolder;
    private static final String AUDIT_LOG_GROUP = "audit-log-group";
    private static final String AUDIT_LOG_TOPIC = "audit-log-topic";
//    private static final String AUDIT_LOG_DLQ = "audit-log-dlq";

    @KafkaListener(groupId = AUDIT_LOG_GROUP, topics = AUDIT_LOG_TOPIC, containerFactory = "auditLogKafkaListenerContainerFactory")
    public void consumeAuditLog(AuditLogRequest auditLogRequest, Acknowledgment ack) {

        String key = auditLogRequest.getEntityName() != null && auditLogRequest.getEntityId() != null
                ? auditLogRequest.getEntityName() + ":" + auditLogRequest.getEntityId()
                : "unknown-key";
//        log.info("Received audit log message: in topic '{}', key={}, request={}", AUDIT_LOG_TOPIC, key, auditLogRequest);

        // Kiểm tra dữ liệu bắt buộc
        if (auditLogRequest.getEntityName() == null || auditLogRequest.getAction() == null) {
            log.error("Missing required fields: entityName or action, key={}, request={}", key, auditLogRequest);
//            sendToDlq(auditLogRequest, key, "Missing required fields");
            ack.acknowledge();
            return;
        }

        try {
            if (auditLogRequest.getTenantId() != null) {
//                log.info("Setting tenantId: {}", auditLogRequest.getTenantId());
                dataSourceContextHolder.setCurrentTenantId(auditLogRequest.getTenantId().longValue());
            } else {
                log.error("Missing required field: tenantId, key={}", key);
//                sendToDlq(auditLogRequest, key, "Missing tenantId");
                ack.acknowledge();
                return;
            }

            AbstractAuditLog auditLog = buildAuditLogFromRequestBuilder(auditLogRequest);
//            log.info("Built audit log: isForUser={}, entityName={}, action={}", auditLogRequest.getIsForUser(), auditLogRequest.getEntityName(), auditLogRequest.getAction());

            log.info("Saving audit log to DB for request: {}", auditLogRequest);
            if ("Y".equals(auditLogRequest.getIsForUser())) {
                auditLogUserRepository.save((AuditLogUser) auditLog);
//                log.info("Successfully saved to d_audit_log_user: key={}", key);
            } else {
                auditLogRepository.save((AuditLog) auditLog);
//                log.info("Successfully saved to d_audit_log: key={}", key);
            }

            ack.acknowledge();

        } catch (Exception e) {
            log.error("Failed to save audit log: key={}, request={}, error={}", key, auditLogRequest, e.getMessage(), e);
//            sendToDlq(auditLogRequest, key, "Failed to save audit log: " + e.getMessage());
            ack.acknowledge();
        }
    }

//    private void sendToDlq(AuditLogRequest auditLogRequest, String key, String errorMessage) {
//        try {
//            kafkaTemplate.send(AUDIT_LOG_DLQ, key, auditLogRequest);
//            log.info("Audit log sent to DLQ: key={}, error={}", key, errorMessage);
//        } catch (Exception dlqEx) {
//            log.error("Failed to send audit log to DLQ: key={}, error={}", key, dlqEx.getMessage(), dlqEx);
//        }
//    }

    private AbstractAuditLog buildAuditLogFromRequestBuilder(AuditLogRequest auditLogRequest) {
        AbstractAuditLog.AbstractAuditLogBuilder<?, ?> builder;
        if ("Y".equals(auditLogRequest.getIsForUser())) {
            builder = AuditLogUser.builder()
                    .entityId(ParseHelper.INT.parse(auditLogRequest.getEntityId()))
                    .description(auditLogRequest.getDescription());
        } else {
            builder = AuditLog.builder()
                    .entityId(ParseHelper.STRING.parse(auditLogRequest.getEntityId()))
                    .fieldName(auditLogRequest.getFieldName())
                    .oldValue(auditLogRequest.getOldValue())
                    .newValue(auditLogRequest.getNewValue());
        }

        builder
                .tenantId(0)
                .orgId(auditLogRequest.getOrgId() != null ? auditLogRequest.getOrgId() : 0)
                .serviceName(auditLogRequest.getServiceName() != null ? auditLogRequest.getServiceName() : "unknown-service")
                .entityName(auditLogRequest.getEntityName())
                .action(auditLogRequest.getAction())
                .userId(auditLogRequest.getUserId() != null ? auditLogRequest.getUserId() : 0)
                .timestamp(auditLogRequest.getTimestamp() != null ? DateHelper.toInstantFromDateTimeString(auditLogRequest.getTimestamp()) : DateHelper.toInstantNowUTC());

        return builder.build();
    }

}
