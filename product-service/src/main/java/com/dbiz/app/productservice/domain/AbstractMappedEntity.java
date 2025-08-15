package com.dbiz.app.productservice.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@EntityListeners(CustomAuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
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


    @Column(name = "created_by")
    private Integer createBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private Integer updateBy;

    @Size(max = 1)
//	@NotNull
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @PrePersist
    protected void onCreate() {
        AuditInfo auditInfo = AuditContext.getAuditInfo();

        
        this.created = Instant.now();
        this.updated = Instant.now();
        this.createBy = AuditContext.getAuditInfo().getUserId();
        this.updateBy = AuditContext.getAuditInfo().getUserId();
        if (this.isActive == null) {
            this.isActive = "Y";
        }

    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = Instant.now();
        this.updateBy = AuditContext.getAuditInfo().getUserId();
    }
}










