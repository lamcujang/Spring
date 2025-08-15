package com.dbiz.app.paymentservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "d_np_transaction", schema = "pos")
public class NapasTransaction extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_np_transaction_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_np_transaction_sq")
    @SequenceGenerator(name = "d_np_transaction_sq", sequenceName = "d_np_transaction_sq", allocationSize = 1)
    private Integer npTransactionId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "case_id")
    private String caseId;

    @Column(name = "creation_date_time")
    private OffsetDateTime creationDateTime;

    @Column(name = "trx_id")
    private String trxId;

    @Column(name = "trans_date_time")
    private OffsetDateTime transDateTime;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "issue_bank")
    private String issueBank;

    @Column(name = "status")
    private String status;

    @Column(name = "beneficiary_bank")
    private String beneficiaryBank;

    @Column(name = "real_merchant_account")
    private String realMerchantAccount;

    @Column(name = "mcc")
    private String mcc;

    @Column(name = "source_account")
    private String sourceAccount;

    @Column(name = "system_trace")
    private String systemTrace;

    @Column(name = "local_time")
    private String localTime;

    @Column(name = "local_date")
    private String localDate;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "ref_id")
    private String refId;


}
