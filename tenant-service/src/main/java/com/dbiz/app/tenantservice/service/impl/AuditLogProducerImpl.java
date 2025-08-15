package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.service.AuditLogProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogProducerImpl implements AuditLogProducer {
    private final KafkaTemplate<String, AuditLogRequest> kafkaTemplate;
    private static final String AUDIT_LOG_TOPIC = "audit-log-topic";
//    private static final String AUDIT_LOG_DLQ = "audit-log-dlq";

    @Override
    public void sendAuditLog(AuditLogRequest auditLogRequest) {
        String key = auditLogRequest.getEntityName() + ":" + auditLogRequest.getEntityId();
        try {
            log.info("Preparing to send audit log with request {}", auditLogRequest);
            kafkaTemplate.send(AUDIT_LOG_TOPIC, key, auditLogRequest);
//                    .addCallback(
//                            (SendResult<String, AuditLogRequest> result) -> log.info("Audit log sent successfully: key={}", key),
//                            ex -> {
//                                log.error("Failed to send audit log to Kafka: key={}, error={}", key, ex.getMessage(), ex);
////                                sendToDlq(auditLogRequest, key);
//                            }
//                    );

        } catch (Exception e) {
            log.error("Error sending audit log to Kafka: key={}, error={}", key, e.getMessage(), e);
//            sendToDlq(auditLogRequest, key);
        }
    }

//    private void sendToDlq(AuditLogRequest auditLogRequest, String key) {
//        try {
//            kafkaTemplate.send(AUDIT_LOG_DLQ, key, auditLogRequest);
//            log.info("Audit log sent to DLQ: key={}", key);
//        } catch (Exception e) {
//            log.error("Failed to send audit log to DLQ: key={}, error={}", key, e.getMessage(), e);
//        }
//    }

}
