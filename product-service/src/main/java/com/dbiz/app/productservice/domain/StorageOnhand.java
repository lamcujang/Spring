package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

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
    @Column(name = "d_storage_onhand_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "qty")
    private BigDecimal qty;


    @NotNull
    @Column(name = "d_warehouse_id")
    private Integer warehouseId;

    @Column(name = "d_locator_id")
    private Integer locatorId;

    @Column(name = "reservation_qty")
    private BigDecimal reservationQty;

    @Size(max = 36)
    @Column(name = "d_storage_onhand_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String storageOnhandUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}