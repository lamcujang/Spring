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
@Table(name = "d_np_reconciliation_dt", schema = "pos")
public class NapasReconciliationDT extends AbstractMappedEntity implements Serializable {


    @Id
    @Column(name = "d_np_reconciliation_dt_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_np_reconciliation_dt_sq")
    @SequenceGenerator(name = "d_np_reconciliation_dt_sq", sequenceName = "d_np_reconciliation_dt_sq", allocationSize = 1)
    private Integer npReconciliationDTId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "trx_id")
    private String trxId;

    @Column(name= "trans_date_time")
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

    @Column(name = "ref_id")
    private String refId;

    @Column(name = "credit_trace")
    private String creditTrace;

    @Column(name = "credit_reference_number")
    private String creditReferenceNumber;

    @Column(name = "reserve_info")
    private String reserveInfo;

    @Column(name = "d_np_reconciliation_id")
    private Integer npReconciliationId;

}

