package com.dbiz.app.userservice.domain;

import lombok.*;

@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class AuditInfo {
    private Integer tenantId;
    private Integer orgId;

    private String createBy;

    private String updateBy;

    private Integer userId;

}
