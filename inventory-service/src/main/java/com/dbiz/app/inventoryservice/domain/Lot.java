package com.dbiz.app.inventoryservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_lot", schema = "pos")
public class Lot extends AbstractMappedEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_lot_sq")
    @SequenceGenerator(name = "d_lot_sq", sequenceName = "d_lot_sq", allocationSize = 1)
    @Column(name = "d_lot_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "code")
    private String code;

    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "d_vendor_id")
    private Integer vendorId;

    @Column(name = "d_warehouse_id")
    private Integer warehouseId;

    @Column(name = "d_locator_id")
    private Integer locatorId;

    @Column(name = "costprice")
    private BigDecimal costPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "manufacture_date")
    private Instant manufactureDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;

}
