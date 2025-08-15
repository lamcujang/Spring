package org.common.dbiz.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Integer id;
    private Integer tenantId;
    private Integer orgId;
    private String serviceName;
    private String entityName;
    private Integer entityId;
    private String action;
    private Integer userId;
    private String uuid;
    private String timestamp;
    private String formattedMessage;
    private String redirectUrl;
}