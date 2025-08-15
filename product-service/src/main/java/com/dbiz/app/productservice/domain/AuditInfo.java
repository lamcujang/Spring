package com.dbiz.app.productservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditInfo {
    private Integer tenantId;
    private Integer userId;
    private Integer orgId;

    private String createBy;

    private String updateBy;




}
