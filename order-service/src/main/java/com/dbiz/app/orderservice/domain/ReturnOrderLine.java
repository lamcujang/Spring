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
@Table(name = "d_return_orderline", schema = "pos")
public class ReturnOrderLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_return_orderline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_return_orderline_sq")
    @SequenceGenerator(name = "d_return_orderline_sq", sequenceName = "d_return_orderline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_return_order_id", precision = 10)
    private Integer returnOrderId;

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "net_return_amount")
    private BigDecimal netReturnAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_return_orderline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dReturnOrderlineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "return_price")
    private BigDecimal returnPrice;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Column(name = "total_discount_amount")
    private BigDecimal totalDiscountAmount;

    @Column(name = "d_purchase_orderline_id")
    private Integer purchaseOrderlineId;

    @Column(name = "d_pos_orderline_id")
    private Integer posOrderlineId;
}