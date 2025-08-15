package com.dbiz.app.tenantservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuditInfo {
    private Integer tenantId;
    private Integer orgId;

    private String createBy;

    private String updateBy;

    private Integer userId;

    private String language;
    private Integer mainTenantId;
}
