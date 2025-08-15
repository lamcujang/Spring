package com.dbiz.app.productservice.domain;

import com.dbiz.app.productservice.domain.view.OrgPriceListV;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {  "uom","image"})
@Table(name = "d_product_combo", schema = "pos")
public class ProductCombo   extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_product_combo_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_product_combo_sq")
    @SequenceGenerator(name = "d_product_combo_sq", sequenceName = "d_product_combo_sq", allocationSize = 1)

    private Integer id;


    @NotNull
    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @NotNull
    @Column(name = "d_product_component_id", nullable = false, precision = 10)
    private Integer productComponentId;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Size(max = 1)
    @Column(name = "is_item", length = 1)
    private String isItem;


    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "sequence", precision = 10)
    private Integer sequence;

}