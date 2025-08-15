package org.common.dbiz.request.tenantRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogRequest extends BaseQueryRequest implements Serializable {

    private String isForUser;

    private Integer id;
    private Integer tenantId;
    private Integer orgId;
    private String serviceName;
    private String entityName;
    private Object entityId;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private String action;
    private Integer userId;
    private String uuid;
    private String timestamp;
    private String description; // for auditLogUser (not used)

}
