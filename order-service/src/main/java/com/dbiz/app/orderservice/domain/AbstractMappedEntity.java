package com.dbiz.app.orderservice.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.*;


import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EntityListeners(CustomAuditingEntityListener.class)
abstract public class AbstractMappedEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @CreatedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "created", updatable = false)
    private Instant created;

    @LastModifiedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "updated")
    private Instant updated;

    @Column(name = "created_by",updatable = false)
    @CreatedBy
    private Integer createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private Integer updatedBy;


    @Column(name = "is_active", columnDefinition = "varchar(1) default 'Y'")
    private String isActive = "Y";

    @Column(name = "d_tenant_id" , updatable = false)
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @PrePersist
    protected void onCreate() {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        if (this.orgId == null) {
            this.orgId = 0;
        }


        this.isActive = "Y";
        this.created = Instant.now();
        this.createdBy = AuditContext.getAuditInfo().getUserId();
        this.updated = Instant.now();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
        this.tenantId = AuditContext.getAuditInfo().getTenantId();

    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
    }
}










