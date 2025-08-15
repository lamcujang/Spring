package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "d_invoice", schema = "pos")
public class Invoice extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_invoice_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_invoice_sq")
    @SequenceGenerator(name = "d_invoice_sq", sequenceName = "d_invoice_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_customer_id")
    private Integer customerId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "d_order_id", precision = 10)
    private Integer orderId;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Size(max = 32)
    @NotNull
    @Column(name = "document_no", nullable = false, length = 32)
    private String documentNo;

    @NotNull
    @Column(name = "date_invoiced", nullable = false)
    private Instant dateInvoiced;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false)
    private Integer docTypeId;

    @NotNull
    @Column(name = "d_currency_id", nullable = false, precision = 10)
    private Integer currencyId;

    @Column(name = "accounting_date")
    private Instant accountingDate;

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

    @Size(max = 255)
    @Column(name = "buyer_company")
    private String buyerCompany;

    @Size(max = 15)
    @Column(name = "buyer_phone", length = 15)
    private String buyerPhone;

    @Size(max = 25)
    @Column(name = "buyer_citizen_id", length = 25)
    private String buyerCitizenId;

    @Size(max = 25)
    @Column(name = "buyer_passport_number")
    private String buyerPassportNumber;

    @Size(max = 25)
    @Column(name = "buyer_budget_unit_code")
    private String buyerBudgetUnitCode;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 5)
    @NotNull
    @Column(name = "invoice_status", nullable = false, length = 5)
    private String invoiceStatus;

    @Size(max = 36)
    @Column(name = "d_invoice_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dInvoiceUu;

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer priceListId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

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

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "einvoice_status")
    private String einvoiceStatus;

    @Column(name = "einvoice_supplier_taxcode")
    private String einvoiceSupplierTaxcode;

    @Column(name = "einvoice_transactionid")
    private String einvoiceTransactionId;

    @Column(name = "einvoice_reservationcode")
    private String einvoiceReservationCode;

    @Column(name = "einvoice_taxcode")
    private String einvoiceCodeOfTax;

    @Column(name = "issued_date")
    private Instant issuedDate;

}