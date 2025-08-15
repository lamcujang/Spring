package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_production", schema = "pos")
public class Production extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_production_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_production_sq")
    @SequenceGenerator(name = "d_production_sq", sequenceName = "d_production_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    private Integer doctypeId;

    @Size(max = 32)
    @NotNull
    @Column(name = "documentno", nullable = false, length = 32)
    private String documentno;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "movement_date")
    private Instant movementDate;

    @Column(name = "production_qty")
    private BigDecimal productionQty;

    @Size(max = 32)
    @NotNull
    @Column(name = "documentstatus", nullable = false, length = 32)
    private String documentStatus;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Size(max = 36)
    @Column(name = "d_production_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String productionUu;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_processed", nullable = false, length = 1)
    private String isProcessed;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_sync_erp", nullable = false, length = 1)
    private String isSyncErp;

    @Column(name = "erp_production_id", precision = 10)
    private Integer erpProductionId;

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Column(name = "d_locator_id", precision = 10)
    private Integer locatorId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}