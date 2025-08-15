package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder

@Table(name = "d_locator", schema = "pos")
public class Locator extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_locator_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_locator_sq")
    @SequenceGenerator(name = "d_locator_sq", sequenceName = "d_locator_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 32)
    @Column(name = "x", length = 32)
    private String x;

    @Size(max = 32)
    @Column(name = "y", length = 32)
    private String y;

    @Size(max = 32)
    @Column(name = "z", length = 32)
    private String z;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "d_warehouse_id", nullable = false)
//    private Warehouse warehouse;
    @Column(name = "d_warehouse_id", nullable = false)
    private Integer warehouseId;

    @Size(max = 36)
//    @NotNull
    @Column(name = "d_locator_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String locatorUu;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "is_default")
    private String isDefault;
}