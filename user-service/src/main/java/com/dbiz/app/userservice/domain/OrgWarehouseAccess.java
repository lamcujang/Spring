package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.Org;
import com.dbiz.app.tenantservice.domain.Tenant;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_org_warehouse_access", schema = "pos")
@IdClass(PrimaryOrgWarehouseAccess.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgWarehouseAccess {

    @Id
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;
    @Id
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;
    @Id
    @Column(name = "d_warehouse_id", nullable = false)
    private Integer warehouseId;

    @Id
    @Column(name = "d_user_id", nullable = false)
    private Integer userId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private BigDecimal createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private BigDecimal updatedBy;

}