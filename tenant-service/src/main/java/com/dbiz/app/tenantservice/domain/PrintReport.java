package com.dbiz.app.tenantservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_print_report", schema = "pos")
public class PrintReport extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_print_report_sq")
    @SequenceGenerator(name = "d_print_report_sq", sequenceName = "d_print_report_sq", allocationSize = 1)
    @Column(name = "d_print_report_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "report_type", nullable = false, length = 32)
    private String reportType;

    @Column(name = "report_source", nullable = false)
    private String reportSource;

    @Size(max = 36)
    @Column(name = "d_print_report_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String printReportUu;

    @Column(name="is_default",columnDefinition = "varchar(1) default 'N'", insertable = true, updatable = true)
    private String isDefault = "N";

    @Column(name = "d_tenant_id")
    private Integer tenantId;



}