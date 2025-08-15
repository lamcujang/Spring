package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_purchase_invoice", schema = "pos")
public class PurchaseInvoice extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_purchase_invoice_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_purchase_invoice_sq")
    @SequenceGenerator(name = "d_purchase_invoice_sq", sequenceName = "d_purchase_invoice_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    Integer orgId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "document_no")
    private String documentNo;

    @Column(name = "date_invoiced")
    private Instant dateInvoiced;

    @Column(name = "description")
    private String description;

    @Column(name = "payment_due_date")
    private Instant paymentDueDate;

    @Column(name = "invoice_status")
    private String invoiceStatus;

    @Column(name = "d_doctype_id")
    private Integer doctypeId;

    @Column(name = "d_currency_id")
    private Integer currencyId;

    @Column(name = "accounting_date")
    private Instant accountingDate;

    @Column(name = "vendor_tax_code")
    private String vendorTaxCode;

    @Column(name = "vendor_address")
    private String vendorAddress;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "d_purchase_invoice_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPurchaseInvoiceUu;

    @Column(name = "d_user_id")
    private Integer userId;

    @Column(name = "reference_purchase_invoice_id")
    private Integer referencePurchaseInvoiceId;

    @Column(name = "invoice_form")
    private String invoiceForm;

    @Column(name = "invoice_sign")
    private String invoiceSign;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "search_code")
    private String searchCode;

    @Column(name = "search_link")
    private String searchLink;

    @Column(name = "issued_date")
    private Instant issuedDate;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "einvoice_status", length = 5)
    private String einvoiceStatus;


    @Column(name = "einvoice_taxcode", length = 32)
    private String einvoiceTaxcode;
}
