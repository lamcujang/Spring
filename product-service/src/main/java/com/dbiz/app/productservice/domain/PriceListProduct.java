package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "d_pricelist_product", schema = "pos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListProduct extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pricelist_product_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pricelist_product_sq")
    @SequenceGenerator(name = "d_pricelist_product_sq", sequenceName = "d_pricelist_product_sq", allocationSize = 1)
    private Integer id;


    @NotNull
    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @Column(name = "costprice")
    private BigDecimal costPrice;

    @Column(name = "standardprice")
    private BigDecimal standardPrice;

    @Column(name = "salesprice")
    private BigDecimal salesPrice;

    @Column(name = "lastorderprice")
    private BigDecimal lastOrderPrice;

    @Size(max = 36)
    @Column(name = "d_pricelist_product_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String pricelistProductUu;

    @NotNull
    @Column(name = "d_pricelist_id", nullable = false, precision = 10)
    private Integer priceListId;


    // view
    @OneToMany(mappedBy = "productId", fetch = FetchType.LAZY)
    private List<AssignOrgProduct> assignOrgProducts;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}