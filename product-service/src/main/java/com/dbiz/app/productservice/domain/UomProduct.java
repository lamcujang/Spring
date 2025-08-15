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
@Table(name = "d_uom_product", schema = "pos")
public class UomProduct extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_uom_product_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_uom_product_sq")
    @SequenceGenerator(name = "d_uom_product_sq", sequenceName = "d_uom_product_sq", allocationSize = 1)

    private Integer id;


    @Column(name = "d_product_id")
    private Integer productId;


    @Column(name = "d_uom_id", length = 15)
    private Integer uomId;


    @Column(name = "conversion_value")
    private BigDecimal conversionValue;

    @Column(name = "costprice")
    private BigDecimal costprice;

    @Size(max = 36)
    @Column(name = "d_uom_product_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String uomProductUu;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}