package com.dbiz.app.systemservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuditInfo {
    private Integer clientId;
    private Integer orgId;

    private String createBy;

    private String updateBy;

    private Integer userId;


}
