package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_return_order", schema = "pos")
public class ReturnOrder extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_return_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_return_order_sq")
    @SequenceGenerator(name = "d_return_order_sq", sequenceName = "d_return_order_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    private Integer doctypeId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @NotNull
    @Column(name = "document_no", nullable = false, length = 32)
    private String documentNo;

    @NotNull
    @Column(name = "order_status", nullable = false, length = 3)
    private String orderStatus;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "description")
    private String description;

    @Column(name = "d_return_order_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dReturnOrderUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_customer_id")
    private Integer customerId;

    @Column(name = "d_currency_id")
    private Integer currencyId;

    @Column(name = "d_pricelist_id")
    private Integer priceListId;

    @Column(name = "d_pos_terminal_id")
    private Integer posTerminalId;

    @Column(name = "d_shift_control_id")
    private Integer shiftControlId;

    @Column(name = "qrcode_payment")
    private String qrcodePayment;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "net_return_amount")
    private BigDecimal netReturnAmount;

    @Column(name = "flat_discount")
    private BigDecimal flatDiscount;

    @Column(name = "flat_amt")
    private BigDecimal flatAmt;

    @Column(name = "receipt_other_amount")
    private BigDecimal receiptOtherAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "return_fee")
    private BigDecimal returnFee;

    @Column(name = "d_locator_id")
    private Integer locatorId;

    @Column(name = "d_return_reason_id")
    private Integer returnReasonId;

    @Column(name = "d_purchase_order_id")
    private Integer purchaseOrderId;

    @Column(name = "d_pos_order_id")
    private Integer posOrderId;
}