package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_invoiceline", schema = "pos")
public class InvoiceLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_invoiceline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_invoiceline_sq")
    @SequenceGenerator(name = "d_invoiceline_sq", sequenceName = "d_invoiceline_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_invoice_id", nullable = false)
    private Integer invoiceId;


    @Column(name = "d_orderline_id", precision = 10)
    private Integer orderLineId;


    @Column(name = "d_pos_orderline_id", precision = 10)
    private Integer posOrderLineId;

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "lineno", precision = 2)
    private BigDecimal lineno;

    @NotNull
    @Column(name = "qty", nullable = false, precision = 5)
    private BigDecimal qty;

    @NotNull
    @Column(name = "price_entered", nullable = false)
    private BigDecimal priceEntered;

    @NotNull
    @Column(name = "linenet_amt", nullable = false)
    private BigDecimal lineNetAmt;

    @NotNull
    @Column(name = "grand_total", nullable = false)
    private BigDecimal grandTotal;

    @Size(max = 36)
    @Column(name = "d_invoiceline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dInvoicelineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}