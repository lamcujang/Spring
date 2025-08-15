package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_invoice_v", schema = "pos")
public class InvoiceView extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_invoice_id", precision = 10)
    private Integer id;


    @Column(name = "accounting_date")
    private Instant accountingDate;

    @Column(name = "date_invoiced")
    private Instant dateInvoiced;

    @Size(max = 32)
    @Column(name = "document_no", length = 32)
    private String documentNo;

    @Column(name = "d_doctype_id", precision = 10)
    private Integer docTypeId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 5)
    @Column(name = "invoice_status", length = 5)
    private String invoiceStatus;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Size(max = 255)
    @Column(name = "vendor_name")
    private String vendorName;

    @Size(max = 255)
    @Column(name = "buyer_name")
    private String buyerName;

    @Size(max = 15)
    @Column(name = "buyer_tax_code", length = 15)
    private String buyerTaxCode;

    @Size(max = 32)
    @Column(name = "buyer_email", length = 32)
    private String buyerEmail;

    @Size(max = 255)
    @Column(name = "buyer_address")
    private String buyerAddress;

    @Size(max = 15)
    @Column(name = "buyer_phone", length = 15)
    private String buyerPhone;

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer priceListId;

    @Size(max = 64)
    @Column(name = "pricelist_name", length = 64)
    private String priceListName;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Size(max = 64)
    @Column(name = "user_name", length = 64)
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "reference_invoice_id", precision = 10)
    private Integer referenceInvoiceId;

    @Size(max = 32)
    @Column(name = "invoice_form", length = 32)
    private String invoiceForm;

    @Size(max = 32)
    @Column(name = "invoice_sign", length = 32)
    private String invoiceSign;

    @Size(max = 15)
    @Column(name = "invoice_no", length = 15)
    private String invoiceNo;

    @Size(max = 32)
    @Column(name = "search_code", length = 32)
    private String searchCode;

    @Size(max = 255)
    @Column(name = "search_link")
    private String searchLink;

    @Size(max = 255)
    @Column(name = "invoice_error")
    private String invoiceError;

    @Column(name = "paid")
    private BigDecimal paid;

    @Size(max = 32)
    @Column(name = "pos_order_no", length = 32)
    private String posOrderNo;

    @Column(name = "d_pos_order_id", precision = 10)
    private BigDecimal dPosOrderId;

    @Size(max = 15)
    @Column(name = "value_status", length = 15)
    private String valueStatus;

    @Size(max = 64)
    @Column(name = "order_status", length = 64)
    private String orderStatus;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "order_guests")
    private Integer orderGuests;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "einvoice_value_status")
    private String einvoiceValueStatus;

    @Column(name = "einvoice_status")
    private String einvoiceStatus;

    @Column(name = "einvoice_supplier_taxcode")
    private String einvoiceSupplierTaxCode;

    @Column(name = "einvoice_transactionid")
    private String einvoiceTransactionId;

    @Column(name = "einvoice_reservationcode")
    private String einvoiceReservationCode;

    @Column(name = "einvoice_taxcode")
    private String einvoiceTaxCode;

    @Column(name = "issued_date ")
    private Instant issuedDate;

    @Column(name = "address")
    private String orgAddress;

    @Column(name = "price_category_code")
    private String priceCateCode;
}