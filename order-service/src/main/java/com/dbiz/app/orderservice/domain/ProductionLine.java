package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_productionline", schema = "pos")
public class ProductionLine extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_productionline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_productionline_sq")
    @SequenceGenerator(name = "d_productionline_sq", sequenceName = "d_productionline_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_production_id", nullable = false, precision = 10)
    private Integer productionId;

    @Column(name = "lineno")
    private Integer lineNo;

    @NotNull
    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @Size(max = 1)
    @Column(name = "is_end_product", nullable = false, length = 1)
    private String isEndProduct;

    @Column(name = "planned_qty")
    private BigDecimal plannedQty;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "used_qty")
    private BigDecimal usedQty;

}