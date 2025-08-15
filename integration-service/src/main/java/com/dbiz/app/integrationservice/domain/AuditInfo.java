package com.dbiz.app.integrationservice.domain;

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


}
