package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_reconciledetail", schema = "pos")
public class ReconcileDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reconciledetail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reconciledetail_sq")
    @SequenceGenerator(name = "d_reconciledetail_sq", sequenceName = "d_reconciledetail_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 36)
    @Column(name = "d_reconciledetail_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dReconciledetailUu;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Size(max = 60)
    @Column(name = "cmd", length = 60)
    private String cmd;

    @Size(max = 30)
    @Column(name = "merchant_code", length = 30)
    private String merchantCode;

    @Size(max = 100)
    @Column(name = "access_code", length = 100)
    private String accessCode;

    @Size(max = 5)
    @Column(name = "version", length = 5)
    private String version;

    @Size(max = 2000)
    @Column(name = "check_sum", length = 2000)
    private String checkSum;

    @Size(max = 2000)
    @Column(name = "error_code", length = 2000)
    private String errorCode;

    @Size(max = 60)
    @Column(name = "transaction_id", length = 60)
    private String transactionId;

    @Size(max = 10)
    @Column(name = "payment_status", length = 10)
    private String paymentStatus;

    @Size(max = 2000)
    @Column(name = "return_check_sum", length = 2000)
    private String returnCheckSum;

    @Size(max = 200)
    @Column(name = "hash_key", length = 200)
    private String hashKey;

    @Size(max = 255)
    @Column(name = "merchant_name")
    private String merchantName;

    @Size(max = 40)
    @Column(name = "terminalid", length = 40)
    private String terminalId;

    @Size(max = 50)
    @Column(name = "user_update", length = 50)
    private String userUpdate;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Size(max = 255)
    @Column(name = "qrcode_payment")
    private String qrcodePayment;

    @Size(max = 128)
    @Column(name = "transaction_no", length = 128)
    private String transactionNo;

    @Size(max = 255)
    @Column(name = "ftcode")
    private String ftCode;

    @Size(max = 32)
    @Column(name = "reference_code", length = 32)
    private String referenceCode;

    @Column(name = "d_bankaccount_id", precision = 10)
    private Integer bankAccountId;

    @Column(name = "d_bank_id", precision = 10)
    private Integer bankId;

    @Size(max = 32)
    @Column(name = "accountno", length = 32)
    private String accountNo;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}