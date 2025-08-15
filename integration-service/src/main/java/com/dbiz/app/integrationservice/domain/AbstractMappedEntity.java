package com.dbiz.app.integrationservice.domain;


import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@EntityListeners(CustomAuditingEntityListener.class)
abstract public class AbstractMappedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @CreatedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "created")
    private Instant created;

    @LastModifiedDate
    @JsonFormat(shape = Shape.STRING)
    @Column(name = "updated")
    private Instant updated;

    @Column(name = "created_by")
    @CreatedBy
    private Integer createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private Integer updatedBy;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "is_active", columnDefinition = "varchar(1) default 'Y'", insertable = false)
    private String isActive = "Y";

    @PrePersist
    protected void onCreate() {
        if (this.tenantId == null) {
            this.tenantId = AuditContext.getAuditInfo().getTenantId();
        }
        if(this.orgId == null){
            this.orgId =0;
        }

        this.created = Instant.now();
        this.updated = Instant.now();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
        this.createdBy = AuditContext.getAuditInfo().getUserId();


    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
        this.updatedBy = AuditContext.getAuditInfo().getUserId();
    }


}










