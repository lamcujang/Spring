package com.dbiz.app.inventoryservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_storage_onhand", schema = "pos")
public class StorageOnhand extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_storage_onhand_sq")
    @SequenceGenerator(name = "d_storage_onhand_sq", sequenceName = "d_storage_onhand_sq", allocationSize = 1)
    @Column(name = "d_storage_onhand_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "min_qty")
    private BigDecimal minQty;

    @Column(name = "max_qty")
    private BigDecimal maxQty;

    @NotNull
    @Column(name = "d_warehouse_id")
    private Integer warehouseId;

    @Column(name = "d_locator_id")
    private Integer locatorId;

    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "reservation_qty")
    private BigDecimal reservationQty;

    @Size(max = 36)
    @Column(name = "d_storage_onhand_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String storageOnhandUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_lot_id")
    private Integer lotId;

}