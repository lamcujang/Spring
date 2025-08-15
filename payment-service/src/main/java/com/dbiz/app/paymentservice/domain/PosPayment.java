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
@Table(name = "d_pos_payment", schema = "pos")
public class PosPayment extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_payment_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_payment_sq")
    @SequenceGenerator(name = "d_pos_payment_sq", sequenceName = "d_pos_payment_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_pos_order_id", nullable = false, precision = 10)
    private Integer posOrderId;

    @Size(max = 3)
    @NotNull
    @Column(name = "payment_method", nullable = false, length = 3)
    private String paymentMethod;

    @Size(max = 36)
    @Column(name = "voucher_code", length = 36)
    private String voucherCode;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 36)
    @Column(name = "transaction_id", length = 36)
    private String transactionId;

    @Column(name = "tip_amount")
    private BigDecimal tipAmount;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @Size(max = 36)
    @Column(name = "d_pos_payment_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPosPaymentUu;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_processed", nullable = false, length = 1)
    private String isProcessed;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_payment_id")
    private Integer paymentId;

}