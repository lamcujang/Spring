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
@Table(name = "d_purchase_invoiceline", schema = "pos")
public class PurchaseInvoiceLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_purchase_invoiceline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_purchase_invoiceline_sq")
    @SequenceGenerator(name = "d_purchase_invoiceline_sq", sequenceName = "d_purchase_invoiceline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    Integer orgId;

    @Column(name = "d_purchase_invoice_id")
    Integer purchaseInvoiceId;

    @Column(name = "d_purchase_order_id", precision = 10)
    private Integer purchaseOrderId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "d_purchase_invoiceline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPurchaseInvoicelineUu;
}
