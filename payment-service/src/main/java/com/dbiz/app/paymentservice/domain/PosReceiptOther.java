package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor

@Table(name = "d_pos_receipt_other", schema = "pos")
public class PosReceiptOther extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_receipt_other_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_receipt_other_sq")
    @SequenceGenerator(name = "d_pos_receipt_other_sq", sequenceName = "d_pos_receipt_other_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "d_receipt_other_id", precision = 10)
    private Integer receiptOtherId;

    @Column(name = "amount_or_percent")
    private BigDecimal amountOrPercent;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "receipt_amount")
    private BigDecimal receiptAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "receipt_wtax_amount")
    private BigDecimal receiptAmountWithTax;

    @Column(name = "is_cal")
    private String isCal;

    @Size(max = 36)
    @Column(name = "d_pos_payment_dt_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPosPaymentDtUu;

    public PosReceiptOther() {

    }
}