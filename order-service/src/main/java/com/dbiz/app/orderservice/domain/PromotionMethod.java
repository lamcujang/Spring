package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_promotion_methods", schema = "pos")
public class PromotionMethod extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_promotion_methods_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_promotion_methods_sq")
    @SequenceGenerator(name = "d_promotion_methods_sq", sequenceName = "d_promotion_methods_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

//    @Column(name = "discount_amount", nullable = false)
//    private BigDecimal discountAmount;

    @Column(name = "promo_percent")
    private BigDecimal promoPercent;

    @Column(name = "promo_amount")
    private BigDecimal promoAmount;

    @Column(name = "percent_max_amt")
    private BigDecimal percentMaxAmt;

    @Column(name = "d_promotion_id", nullable = false)
    private Integer promotionId;

    @Column(name = "d_product_id", nullable = false)
    private Integer productId;

    @Column(name = "d_promo_product_id", nullable = false)
    private Integer promoProductId;

    @Column(name = "d_product_category_id", nullable = false)
    private Integer productCategoryId;

    @Column(name = "d_promo_category_id", nullable = false)
    private Integer promoCategoryId;

    @Column(name = "point_qty")
    private BigDecimal pointQty;

    @Column(name = "product_qty")
    private BigDecimal productQty;

    @Column(name="sales_price")
    private BigDecimal salesPrice;

    @Column(name = "promo_product_qty")
    private BigDecimal promoProductQty;

    @Column(name = "is_all_category")
    private String isAllCategory;

    @Column(name = "is_all_promo_category")
    private String isAllPromoCategory;

}