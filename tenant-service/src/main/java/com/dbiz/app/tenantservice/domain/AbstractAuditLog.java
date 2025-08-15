package com.dbiz.app.tenantservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@SuperBuilder
abstract public class AbstractAuditLog implements Serializable {

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "d_user_id")
    private Integer userId;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "timestamp", updatable = false, nullable = false)
    private Instant timestamp;

}
