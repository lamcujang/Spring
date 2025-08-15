package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.dbiz.app.tenantservice.domain.Org;
import com.dbiz.app.tenantservice.domain.Tenant;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_product_location", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLocation extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_product_location_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_product_location_sq")
    @SequenceGenerator(name = "d_product_location_sq", sequenceName = "d_product_location_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_warehouse_id", nullable = false, precision = 10)
    private Integer warehouseId;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @Column(name = "erp_product_location_id", precision = 10)
    private Integer erpProductLocationId;

    @Size(max = 1)
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;


    @Column(name = "d_locator_id", precision = 10)
    private Integer locatorId;

    @Column(name = "stock_qty", precision = 10)
    private BigDecimal stockQty;

    @Column(name = "min_qty", precision = 10)
    private BigDecimal minQty;

    @Column(name = "max_qty", precision = 10)
    private BigDecimal maxQty;

}