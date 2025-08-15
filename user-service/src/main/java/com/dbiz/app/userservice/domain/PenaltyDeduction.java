package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_penalty_deduction", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyDeduction extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_penalty_deduction_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_penalty_deduction_sq")
    @SequenceGenerator(name = "d_penalty_deduction_sq", sequenceName = "d_penalty_deduction_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_salary_config_id")
    private Integer salaryConfigId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "penalty_amount")
    private BigDecimal penaltyAmount;

    @Column(name = "warning_count")
    private Integer warningCount;

    @Column(name = "value")
    private String value;
}
