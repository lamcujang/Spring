package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_image", schema = "pos")

public class Image   implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_image_sq")
    @SequenceGenerator(name = "d_image_sq", sequenceName = "d_image_sq", allocationSize = 1)
    @Column(name = "d_image_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created",updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "updated")
    private Instant updatedAt;

    @Column(name = "d_tenant_id")
    private Integer dTenantId;

    @Column(name="created_by")
    private  Integer createBy;

    @Column(name="updated_by")
    @LastModifiedBy
    private  Integer updateBy;

    @Size(max = 1)
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @Size(max = 15)
    @Column(name = "image_code", length = 15)
    private String imageCode;

    @PrePersist
    protected void onCreate() {
        com.dbiz.app.tenantservice.domain.AuditInfo auditInfo = com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo();
            if(this.createBy == null){
                this.createBy =  auditInfo.getUserId();
            }
            if(this.updateBy == null){
                this.updateBy =  auditInfo.getUserId();
            }
            if(this.dTenantId == null){
                this.dTenantId =  auditInfo.getTenantId();
            }
            if(this.isActive == null){
                this.isActive = "Y";
            }
            this.createdAt = Instant.now();
            this.updatedAt = Instant.now();
    }
    @PreUpdate
    protected void onUpdate() {
        AuditInfo auditInfo = AuditContext.getAuditInfo();
        this.updateBy = auditInfo.getUserId();
        this.createBy = auditInfo.getUserId();
        this.updatedAt = Instant.now();
    }
}