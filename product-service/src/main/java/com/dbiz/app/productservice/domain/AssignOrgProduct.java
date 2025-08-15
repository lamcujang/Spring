package com.dbiz.app.productservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_assign_org_product", schema = "pos")
public class AssignOrgProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_assign_org_sq")
    @SequenceGenerator(name = "d_assign_org_sq", sequenceName = "d_assign_org_sq", allocationSize = 1)
    @Column(name = "d_assign_org_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;


    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 36)
    @Column(name = "d_assign_org_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String assignOrgUu;


    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created",updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "updated")
    private Instant updatedAt;


    @Column(name="created_by")
    private  Integer createBy;

    @Column(name="updated_by")
    @LastModifiedBy
    private  Integer updateBy;

    @Size(max = 1)
//	@NotNull
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;



    @PrePersist
    protected void onCreate() {
        AuditInfo auditInfo = AuditContext.getAuditInfo();

        if(this.createdAt == null)
        {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
        if(this.isActive == null)
        {
            this.isActive = "Y";
        }
        this.createBy = com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getUserId();
        this.tenantId =  com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getTenantId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
        this.updateBy =  com.dbiz.app.tenantservice.domain.AuditContext.getAuditInfo().getUserId();
    }
}