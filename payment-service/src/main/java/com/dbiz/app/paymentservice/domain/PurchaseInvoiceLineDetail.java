package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_purchase_invoiceline_detail", schema = "pos")
public class PurchaseInvoiceLineDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_purchase_invoiceline_detail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_purchase_invoiceline_detail_sq")
    @SequenceGenerator(name = "d_purchase_invoiceline_detail_sq", sequenceName = "d_purchase_invoiceline_detail_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_purchase_invoice_id")
    private Integer purchaseInvoiceId;

    @Column(name = "d_purchase_invoiceline_id")
    private Integer purchaseInvoicelineId;

    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "d_tax_id")
    private Integer taxId;

    @Column(name = "d_uom_id")
    private Integer uomId;

    @Column(name = "d_purchase_orderline_id")
    private Integer purchaseOrderlineId;

    @Column(name = "lineno")
    private Integer lineNo;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "price_entered")
    private BigDecimal priceEntered;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_discount_amount")
    private BigDecimal totalDiscountAmount;

    @Column(name = "d_purchase_invoiceline_detail_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPurchaseInvoicelineDetailUu;

    @Column(name = "description")
    private String description;
}
