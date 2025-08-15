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

@Getter
@Setter
@Entity
@Table(name = "d_payment", schema = "pos")
public class Payment extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_payment_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_payment_sq")
    @SequenceGenerator(name = "d_payment_sq", sequenceName = "d_payment_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    private Integer docTypeId;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "d_invoice_id", precision = 10)
    private Integer invoiceId;

    @Column(name = "d_bankaccount_id", precision = 10)
    private Integer bankAccountId;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @NotNull
    @Column(name = "d_currency_id", nullable = false, precision = 10)
    private Integer currencyId;

    @Size(max = 5)
    @NotNull
    @Column(name = "payment_status", nullable = false, length = 5)
    private String paymentStatus;

    @NotNull
    @Column(name = "payment_amount", nullable = false)
    private BigDecimal paymentAmount;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Size(max = 32)
    @NotNull
    @Column(name = "document_no", nullable = false, length = 32)
    private String documentNo;

    @Size(max = 36)
    @Column(name = "d_payment_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPaymentUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "user_group")
    private String userGroup;

    @Column(name = "d_user_id")
    private Integer userId;

    @Column(name = "d_user_other_id")
    private Integer userOtherId;

    @Column(name = "d_pos_terminal_id")
    private Integer posTerminalId;

    @Column(name = "d_return_order_id")
    private Integer returnOrderId;

    @Column(name = "d_purchase_order_id")
    private Integer purchaseOrderId;

    @Column(name = "qrcode")
    private String qrCode;

    @Column(name = "d_purchase_invoice_id")
     private Integer purchaseInvoiceId;
}