package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_purchase_orderline", schema = "pos")
public class PurchaseOrderLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_purchase_orderline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_purchase_orderline_sq")
    @SequenceGenerator(name = "d_purchase_orderline_sq", sequenceName = "d_purchase_orderline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_purchase_order_id", precision = 10)
    private Integer purchaseOrderId;

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "d_uom_id", precision = 10)
    private Integer uomId;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "priceentered")
    private BigDecimal priceEntered;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_purchase_orderline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPurchaseOrderlineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Column(name = "total_discount_amount")
    BigDecimal totalDiscountAmount;

    @Column(name = "d_lot_id")
    private Integer lotId;
}