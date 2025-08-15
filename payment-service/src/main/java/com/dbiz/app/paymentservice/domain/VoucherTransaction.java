package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "d_voucher_transaction", schema = "pos")
public class VoucherTransaction extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_voucher_transaction_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_voucher_transaction_sq")
    @SequenceGenerator(name = "d_voucher_transaction_sq", sequenceName = "d_voucher_transaction_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false, precision = 10)
    private Integer tenantId;

    @NotNull
    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "d_bankaccount_id", precision = 10)
    private Integer bankAccountId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Size(max = 255)
    @NotNull
    @Column(name = "voucher_code", nullable = false)
    private String voucherCode;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Size(max = 36)
    @Column(name = "d_voucher_transaction_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dVoucherTransactionUu;

}