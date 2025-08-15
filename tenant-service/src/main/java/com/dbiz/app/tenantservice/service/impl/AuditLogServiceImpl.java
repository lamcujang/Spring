package com.dbiz.app.tenantservice.service.impl;

import com.dbiz.app.tenantservice.domain.AuditLogUser;
import com.dbiz.app.tenantservice.enums.ParseHelper;
import com.dbiz.app.tenantservice.repository.AuditLogUserRepository;
import com.dbiz.app.tenantservice.service.AuditLogService;
import com.dbiz.app.tenantservice.common.QueryEngine;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.tenantRequest.AuditLogRequest;
import org.common.dbiz.response.AuditLogResponse;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogUserRepository auditLogUserRepository;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;
    private final QueryEngine queryEngine;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    private static final Map<String, String> actionVietnamese = Map.of(
            "DELETE", "xóa",
            "UPDATE", "cập nhật",
            "INSERT", "tạo",
            "CANCEL", "hủy"
    );

    @Override
    public GlobalReponsePagination getAllAuditLog(AuditLogRequest auditLogRequest) {
        log.info("AuditLogRequest: {}", auditLogRequest);

        Parameter parameter = new Parameter();
        parameter.add("d_audit_log_user_id", auditLogRequest.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", auditLogRequest.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("service_name", auditLogRequest.getServiceName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("entity_name", auditLogRequest.getEntityName(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("entity_id", auditLogRequest.getEntityId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("action", auditLogRequest.getAction(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("d_user_id", auditLogRequest.getUserId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("timestamp", Param.getBetweenParam(auditLogRequest.getTimestamp(), auditLogRequest.getTimestamp()), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        parameter.add("description", auditLogRequest.getDescription(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        parameter.add("d_audit_log_user_uu", auditLogRequest.getUuid(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        auditLogRequest.setSortBy("timestamp");
        auditLogRequest.setOrder("asc");

        ResultSet rs = queryEngine.getRecords("pos.d_audit_log_user", parameter, auditLogRequest);

        List<AuditLogResponse> data = new ArrayList<>();
        try {
            while (rs.next()) {
                try {
                    Integer tenantId = rs.getInt("d_tenant_id");
                    Integer entityId = rs.getInt("entity_id");
                    Integer userId = rs.getInt("d_user_id");
                    String entityName = rs.getString("entity_name");
                    String action = rs.getString("action");
                    String description = rs.getString("description");
                    log.info("Processing audit log: tenantId={}, entityId={}, userId={}, entityName={}, action={},description={}", tenantId, entityId, userId, entityName, action, description);

                    // d_config lúc trước có 2 tác dụng: filter entity và query thông tin entity cho AuditLog

                    // Lúc trước AuditLog intercept và lưu tất cả thay đổi Hibernate => Lúc lấy lên phải filter ra những đối tượng quan tâm
                    // Giờ chủ động gửi lưu AuditLogUser => Mọi dữ liệu ở d_audit_log_user đều quan tâm

                    // Nếu dùng query thì AuditLog sẽ Đưa thông tin hiện có của sản phẩm chứ ko phải lúc thực hiện thay đổi và Ko lấy đc thông tin của entity đã bị xóa

                    // Lấy thông tin user từ pos.d_user
                    Map<String, Object> userData = executeUserQuery(userId);
                    String userName = (String) userData.getOrDefault("full_name", "Unknown User");

                    Map<String, Object> result = new HashMap<>();
                    if (description != null && !description.isEmpty()) {
                        JsonNode entityData = objectMapper.readTree(description);
                        result.put("name", entityData.path("name").asText(null));
                        result.put("document_no", entityData.path("document_no").asText(null));
                    }
                    else {
                        result.put("name", "Unknown Entity");
                        result.put("document_no", "Unknown ID");
                    }
                    String name = ParseHelper.STRING.parse(result.get("name"));
                    String documentNo = ParseHelper.STRING.parse(result.get("document_no"));

                    // Lấy template từ pos.d_message
                    String formattedMessage = getMessage(entityName, userName, action, name, documentNo);

                    // Tạo redirectUrl
                    String redirectUrl = generateRedirectUrl(entityName, entityId);

                    AuditLogResponse auditLog = AuditLogResponse.builder()
                            .id(rs.getInt("d_audit_log_user_id"))
                            .tenantId(tenantId)
                            .orgId(rs.getInt("d_org_id"))
                            .serviceName(rs.getString("service_name"))
                            .entityName(entityName)
                            .entityId(entityId)
                            .action(action)
                            .userId(userId)
                            .uuid(rs.getString("d_audit_log_user_uu"))
                            .timestamp(DateHelper.fromTimestampStd(rs.getTimestamp("timestamp")))
                            .formattedMessage(formattedMessage)
                            .redirectUrl(redirectUrl)
                            .build();
                    data.add(auditLog);
                } catch (Exception e) {
                    log.error("Error processing audit log for record with id {}: {}", rs.getInt("d_audit_log_user_id"), e.getMessage());
                    AuditLogResponse defaultLog = AuditLogResponse.builder()
                            .id(rs.getInt("d_audit_log_user_id"))
                            .formattedMessage("Error processing audit log: " + e.getMessage())
                            .build();
                    data.add(defaultLog);
                }
            }
        } catch (Exception e) {
            log.error("Error iterating ResultSet: {}", e.getMessage());
        }

        Pagination pagination = queryEngine.getPagination("pos.d_audit_log_user", parameter, auditLogRequest);
        log.info("Loaded pagination for audit logs...");

        return GlobalReponsePagination.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    // Tạo redirectUrl dựa trên entityName và entityId
    private String generateRedirectUrl(String entityName, Integer entityId) {
        if (entityName == null || entityId == null) {
            log.debug("Cannot generate redirectUrl: entityName or entityId is null");
            return null;
        }

        // Tạo formattedEntityName và thêm hậu tố "s"
        String formattedEntityName = formatEntityName(entityName);
        String redirectUrl = String.format("/%s/%d", formattedEntityName, entityId);
        log.debug("Generated redirectUrl for entityName={} and entityId={}: {}", entityName, entityId, redirectUrl);
        return redirectUrl;
    }

    // Chuyển entityName thành định dạng phù hợp và thêm hậu tố "s"
    private String formatEntityName(String entityName) {
        // Loại bỏ tiền tố 'd_' nếu có
        String cleanedEntityName = entityName.startsWith("d_") ? entityName.substring(2) : entityName;

        // Tách từ bằng CamelCase hoặc dấu gạch dưới
        List<String> words = new ArrayList<>();
        String[] parts = cleanedEntityName.contains("_") ? cleanedEntityName.split("_") : new String[]{cleanedEntityName};
        for (String part : parts) {
            Pattern pattern = Pattern.compile("[A-Z][a-z]*");
            Matcher matcher = pattern.matcher(part);
            while (matcher.find()) {
                words.add(matcher.group());
            }
        }

        if (words.isEmpty()) {
            // Dự phòng: Nếu không tách được từ, trả về entityName thường + "s"
            return cleanedEntityName.toLowerCase() + "s";
        }

        // Chuyển từ đầu tiên thành chữ thường, giữ nguyên các từ sau
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            if (i == 0) {
                formatted.append(words.get(i).toLowerCase());
            } else {
                formatted.append(words.get(i));
            }
        }
        formatted.append("s");
        return formatted.toString();
    }

    // version 2 executeUserQuery
    private Map<String, Object> executeUserQuery(Integer userId) {
        String sql = "SELECT full_name FROM pos.d_user WHERE d_user_id = :userId";
        try {
            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("userId", userId)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List<Map<String, Object>> results = query.getResultList();
            log.debug("User query result for userId={}: {}", userId, results);
            return results.isEmpty() ? new HashMap<>() : results.get(0);
        } catch (Exception e) {
            log.error("Error executing user query for userId={}: {}", userId, e.getMessage());
            return new HashMap<>();
        }
    }

    // Lấy template từ pos.d_message dựa trên entityName và build message
    private String getMessage(String entityName, String userName, String action, String name, String documentNo) {
        // Loại bỏ tiền tố 'd_' nếu có để khớp với D_PRODUCT_MESSAGE_TYPE, D_POS_ORDER_MESSAGE_TYPE
        String configEntityName = entityName.startsWith("d_") ? entityName.substring(2) : entityName;
        String value = "D_" + configEntityName.toUpperCase() + "_MESSAGE_TYPE";
        String sql = "SELECT msg_text FROM pos.d_message WHERE value = :value";
        try {
            Query query = entityManager.createNativeQuery(sql)
                    .setParameter("value", value)
                    .unwrap(NativeQuery.class)
                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
            List<Map<String, Object>> results = query.getResultList();
            log.debug("Message template query for entityName={}, value={}: {}", entityName, value, results);
            String messageTemplate = results.isEmpty() ? "Người dùng @User@ đã thực hiện hành động trên @name@" : (String) results.get(0).get("msg_text");
            return messageTemplate
                    .replace("@User@", userName)
                    .replace("@action@", actionVietnamese.get(action))
                    .replace("@name@", name != null ? name : "Unknown Entity")
                    .replace("@Bang_LoaiChungTu_Id@", documentNo != null ? documentNo : "Unknown ID");
        } catch (Exception e) {
            log.error("Error fetching message template for entity {}: {}", entityName, e.getMessage());
            return null;
        }
    }

/// /////////////////////////////////////
    // version 1 executeUserQuery
//    // Thực thi câu lệnh SQL để lấy thông tin user từ pos.d_user
//    private Map<String, Object> executeUserQuery(Integer userId) {
//        String sql = getConfigSql("USER"); // Sử dụng 'USER' để lấy D_USER_SQL_TYPE
//        if (sql == null) {
//            log.debug("No config found for USER in pos.d_config");
//            return new HashMap<>();
//        }
//        sql = sql.replace("@id@", userId != null ? userId.toString() : "NULL");
//
//        try {
//            Query query = entityManager.createNativeQuery(sql)
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//            List<Map<String, Object>> results = query.getResultList();
//            log.debug("User query result for userId={}: {}", userId, results);
//            return results.isEmpty() ? new HashMap<>() : results.get(0);
//        } catch (Exception e) {
//            log.error("Error executing user query for userId={}: {}", userId, e.getMessage());
//            return new HashMap<>();
//        }
//    }
/// /////////////////////////////////////
    // version 1 executeEntityQuery
//    // Thực thi câu lệnh SQL để lấy thông tin entity
//    private Map<String, Object> executeEntityQuery(String entityName, Integer entityId, String action) {
//        String sql;
//
//        // Kiểm tra nếu hành động là DELETE
//        if ("DELETE".equalsIgnoreCase(action)) {
//            sql = getConfigSql("DELETE"); // Lấy SQL từ D_DELETE_SQL_TYPE
//            if (sql == null) {
//                log.debug("No config found for DELETE in pos.d_config");
//                return new HashMap<>();
//            }
//        } else {
//            // Xử lý cho INSERT và UPDATE
//            sql = getConfigSql(entityName);
//            if (sql == null) {
//                log.debug("No config found for entity {} in pos.d_config", entityName);
//                return new HashMap<>();
//            }
//        }
//
//        sql = sql.replace("@id@", entityId != null ? entityId.toString() : "NULL");
//        try {
//            Query query = entityManager.createNativeQuery(sql)
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//            List<Map<String, Object>> results = query.getResultList();
//            log.debug("Entity query result for entityName={}, entityId={}, action={}: {}", entityName, entityId, action, results);
//            return results.isEmpty() ? new HashMap<>() : results.get(0);
//        } catch (Exception e) {
//            log.error("Error executing entity query for entityName={}, entityId={}, action={}: {}", entityName, entityId, action, e.getMessage());
//            return new HashMap<>();
//        }
//    }
    /// /////////////////////////////
//    // Lấy câu lệnh SQL từ pos.d_config dựa trên entityName
//    private String getConfigSql(String entityName) {
//        // Loại bỏ tiền tố 'd_' nếu có để khớp với D_PRODUCT_SQL_TYPE, D_POS_ORDER_SQL_TYPE
//        String configEntityName = entityName.startsWith("d_") ? entityName.substring(2) : entityName;
//        String configName = "D_" + configEntityName.toUpperCase() + "_SQL_TYPE";
//        String sql = "SELECT value FROM pos.d_config WHERE name = :name";
//        try {
//            Query query = entityManager.createNativeQuery(sql)
//                    .setParameter("name", configName)
//                    .unwrap(NativeQuery.class)
//                    .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//            List<Map<String, Object>> results = query.getResultList();
//            log.debug("Config query for entityName={}, configName={}: {}", entityName, configName, results);
//            return results.isEmpty() ? null : (String) results.get(0).get("value");
//        } catch (Exception e) {
//            log.error("Error fetching config for entity {}: {}", entityName, e.getMessage());
//            return null;
//        }
//    }
    /// ////////////////////////////

}
