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
@Table(name = "d_np_reconciliation", schema = "pos")
public class NapasReconciliation extends AbstractMappedEntity implements Serializable {


    @Id
    @Column(name = "d_np_reconciliation_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_np_reconciliation_sq")
    @SequenceGenerator(name = "d_np_reconciliation_sq", sequenceName = "d_np_reconciliation_sq", allocationSize = 1)
    private Integer npReconciliationId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "begin_date")
    private OffsetDateTime beginDate;

    @Column(name = "end_date")
    private OffsetDateTime endDate;

    @Column(name = "receiving_report")
    private String receivingReport;

    @Column(name = "report_id")
    private String reportId;

    @Column(name = "creation_date_time")
    private OffsetDateTime creationDateTime;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "total_page")
    private Integer totalPage;


}

