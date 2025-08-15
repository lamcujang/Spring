package com.dbiz.app.tenantservice.listener;

import com.dbiz.app.tenantservice.domain.AuditLog;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditLogUser;
import com.dbiz.app.tenantservice.service.AuditLogProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DbMetadataHelper;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.core.env.Environment;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditEntityInterceptor extends EmptyInterceptor {

    private final Environment environment;
    private final AuditLogProducer auditLogProducer;
    private final ObjectMapper objectMapper;

    // Map<entityName-rowId, Map<fieldName, FieldChange>>
    private final ThreadLocal<Map<String, Map<String, FieldChange>>> pendingChanges =
            ThreadLocal.withInitial(HashMap::new);

    // helper class to hold the first-old and last-new values
    @AllArgsConstructor
    private static class FieldChange {
        final String oldValue;
        String newValue;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
                                Object[] previousState, String[] propertyNames, Type[] types) {

        log.info("onFlushDirty triggered for entity: {}", entity.getClass().getSimpleName());

        if (entity instanceof AuditLog || entity instanceof AuditLogUser) {
            log.warn("Skipping audit log for UPDATE on AuditLog entity to prevent loop");
            return false;
        }

        String entityName = DbMetadataHelper.getTableName(entity);
        String entityKey = entityName + "-" + id;
        Map<String, Map<String, FieldChange>> txMap = pendingChanges.get();
        Map<String, FieldChange> fieldMap = txMap.computeIfAbsent(entityKey, k -> new HashMap<>());

        for (int i = 0; i < propertyNames.length; i++) {

            String propertyName = propertyNames[i];
            String fieldName = DbMetadataHelper.getColumnName(entity, propertyName);

//            if (loggedFields.contains(fieldName)) {
//                continue; // Skip if already logged in this transaction
//            }

            // entity skipped fields: created, updated, createdBy, createdBy, createdAt, updatedAt, createBy, updateBy
            if (fieldName.equalsIgnoreCase("created") ||
                    fieldName.equalsIgnoreCase("updated") ||
                    fieldName.equalsIgnoreCase("created_by") ||
                    fieldName.equalsIgnoreCase("updated_by") )
            {
                continue;
            }

            Object previousValue = previousState[i];
            Object currentValue = currentState[i];

            // Bỏ qua nếu cả hai giá trị đều null hoặc bằng nhau
            if (Objects.equals(previousValue, currentValue)) {
                continue;
            }

            // Xử lý đặc biệt cho trường kiểu Double hoặc BigDecimal
            if (previousValue instanceof Double && currentValue instanceof Double) {
                Double prevDouble = (Double) previousValue;
                Double currDouble = (Double) currentValue;
                // So sánh giá trị số thực với độ chính xác nhỏ
                if (Math.abs(prevDouble - currDouble) < 0.0001) {
                    log.debug("Skipping audit log for field {}: values {} and {} are numerically equivalent", fieldName, prevDouble, currDouble);
                    continue;
                }
            } else if (previousValue instanceof BigDecimal && currentValue instanceof BigDecimal) {
                BigDecimal prevBigDecimal = (BigDecimal) previousValue;
                BigDecimal currBigDecimal = (BigDecimal) currentValue;
                // So sánh giá trị BigDecimal chính xác
                if (prevBigDecimal.compareTo(currBigDecimal) == 0) {
                    log.debug("Skipping audit log for field {}: values {} and {} are numerically equivalent", fieldName, prevBigDecimal, currBigDecimal);
                    continue;
                }
            }

            String oldValueString = previousValue != null ? previousValue.toString() : "null";
            String newValueString = currentValue != null ? currentValue.toString() : "null";
            FieldChange change = fieldMap.get(fieldName);
            if (change == null) {
                fieldMap.put(fieldName, new FieldChange(oldValueString, newValueString));
            } else {
                change.newValue = newValueString;
            }

        }
        return false;
    }

    // Clear the tracking map after the transaction
    @Override
    public void afterTransactionCompletion(Transaction tx) {

        Map<String, Map<String, FieldChange>> txMap = pendingChanges.get();
//        if (txMap.isEmpty()) {
//            log.warn("Current Transaction have no changes, skipping audit log for UPDATE"); // stand-alone repo.save()
//            return;
//        }

        try {
            if (AuditContext.getAuditInfo() == null) {
                log.warn("AuditContext is null, skipping audit log for UPDATE for changes: {}", new ArrayList<>(txMap.keySet()));
                return;
            }

            Integer tenantId = AuditContext.getAuditInfo().getMainTenantId();
            Integer orgId = AuditContext.getAuditInfo().getOrgId();
            String serviceName = environment.getProperty("spring.application.name", "unknown-service");
            Integer userId = AuditContext.getAuditInfo().getUserId();

            if (tenantId == null || orgId == null || userId == null) {
                log.warn("Incomplete AuditContext data, skipping audit log for UPDATE");
                return;
            }

            for (Map.Entry<String, Map<String, FieldChange>> rowMapEntry : txMap.entrySet()) {

                String entityName = rowMapEntry.getKey().split("-", 2)[0];
                Integer id = Integer.valueOf(rowMapEntry.getKey().split("-", 2)[1]);
//                String[] parts = rowMapEntry.getKey().split("_");
//                String idStr = parts[parts.length - 1];
//                Integer id = Integer.valueOf(idStr);

                for (Map.Entry<String, FieldChange> fieldMapEntry : rowMapEntry.getValue().entrySet()) {

                    String fieldName = fieldMapEntry.getKey();
                    String oldValue = fieldMapEntry.getValue().oldValue;
                    String newValue = fieldMapEntry.getValue().newValue;

                    AuditLogRequest auditLog = buildAuditLogRequest(
                            tenantId,
                            orgId,
                            serviceName,
                            entityName,
                            getStringId(id),
                            fieldName,
                            oldValue,
                            newValue,
                            "UPDATE",
                            userId
                    );

                    try {
                        auditLogProducer.sendAuditLog(auditLog);
//                        log.info("Audit log sent successfully for UPDATE on entity: {}, field: {}", entityName, fieldName);
                    } catch (Exception e) {
                        log.error("Failed to send audit log for UPDATE on entity: {}, field: {}, error: {}",
                                entityName, fieldName, e.getMessage(), e);
                    }
                }
            }

        } catch (Exception ignored) {
            log.info("Error from sending AuditLog: ", ignored);
        } finally {
            pendingChanges.remove();
            super.afterTransactionCompletion(tx);
        }

    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state,
                          String[] propertyNames, Type[] types) {

        log.info("onSave triggered for entity: {}", entity.getClass().getSimpleName());

        if (entity instanceof AuditLog || entity instanceof AuditLogUser) {
            log.warn("Skipping audit log for INSERT on AuditLog entity to prevent loop");
            return false;
        }

        String entityName = DbMetadataHelper.getTableName(entity);
        if (AuditContext.getAuditInfo() == null) {
            log.warn("AuditContext is null, skipping audit log for INSERT on entity: {}", entityName);
            return false;
        }

        Integer tenantId = AuditContext.getAuditInfo().getMainTenantId();
        Integer orgId = AuditContext.getAuditInfo().getOrgId();
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        Integer userId = AuditContext.getAuditInfo().getUserId();

        if (tenantId == null || orgId == null || userId == null) {
            log.warn("Incomplete AuditContext data, skipping audit log for INSERT on entity: {}", entityName);
            return false;
        }

        AuditLogRequest auditLog = buildAuditLogRequest(
                tenantId,
                orgId,
                serviceName,
                entityName,
                getStringId(id),
                null,
                null,
                getStringId(id),
                "INSERT",
                userId
        );

        try {
            auditLogProducer.sendAuditLog(auditLog);
//            log.info("Audit log sent successfully for INSERT on entity: {}", entityName);
        } catch (Exception e) {
            log.error("Failed to send audit log for INSERT on entity: {}, error: {}",
                    entityName, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state,
                         String[] propertyNames, Type[] types) {

        log.info("onDelete triggered for entity: {}", entity.getClass().getSimpleName());

        if (entity instanceof AuditLog || entity instanceof AuditLogUser) {
            log.warn("Skipping audit log for DELETE on AuditLog entity to prevent loop");
            return;
        }

        String entityName = DbMetadataHelper.getTableName(entity);
        if (AuditContext.getAuditInfo() == null) {
            log.warn("AuditContext is null, skipping audit log for DELETE on entity: {}", entityName);
            return;
        }

        Integer tenantId = AuditContext.getAuditInfo().getMainTenantId();
        Integer orgId = AuditContext.getAuditInfo().getOrgId();
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        Integer userId = AuditContext.getAuditInfo().getUserId();

        if (tenantId == null || orgId == null || userId == null) {
            log.warn("Incomplete AuditContext data, skipping audit log for DELETE on entity: {}", entityName);
            return;
        }

        AuditLogRequest auditLog = buildAuditLogRequest(
                tenantId,
                orgId,
                serviceName,
                entityName,
                getStringId(id),
                null,
                getStringId(id),
                null,
                "DELETE",
                userId
        );

        try {
            auditLogProducer.sendAuditLog(auditLog);
//            log.info("Audit log sent successfully for DELETE on entity: {}", entityName);
        } catch (Exception e) {
            log.error("Failed to send audit log for DELETE on entity: {}, error: {}",
                    entityName, e.getMessage(), e);
        }
    }

    private AuditLogRequest buildAuditLogRequest(
            Integer tenantId,
            Integer orgId,
            String serviceName,
            String entityName,
            String entityId,
            String fieldName,
            String oldValue,
            String newValue,
            String action,
            Integer userId
    ) {
        return AuditLogRequest.builder()
                .isForUser("N")
                .tenantId(tenantId)
                .orgId(orgId)
                .serviceName(serviceName)
                .entityName(entityName)
                .entityId(entityId)
                .fieldName(fieldName)
                .oldValue(oldValue)
                .newValue(newValue)
                .action(action)
                .userId(userId)
                .timestamp(DateHelper.toInstantNowUTC().toString())
                .build();
    }

    private String getStringId(Serializable id) {
        if (id == null) {
            return "Id null";
        }
        if (id instanceof Integer) {
            return String.valueOf(id);
        } else {
            try {
                return objectMapper.writeValueAsString(id);
            } catch (Exception e) {
                log.error("Failed to call objectMapper.writeValueAsString(Serializable id) for id {}, error: ", id, e);
                return "objectMapper.writeValueAsString(id) threw";
            }
        }
    }

}
