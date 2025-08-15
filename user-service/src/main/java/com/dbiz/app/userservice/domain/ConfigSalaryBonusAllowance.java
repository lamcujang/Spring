package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_config_salary_bonus_allowances", schema = "pos")
public class ConfigSalaryBonusAllowance extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_config_salary_bonus_allowances_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_config_salary_bonus_allowances_sq")
    @SequenceGenerator(name = "d_config_salary_bonus_allowances_sq", sequenceName = "d_config_salary_bonus_allowances_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_bonus_id")
    private Integer bonusId;

    @Column(name = "d_allowance_id")
    private Integer allowanceId;

    @Column(name = "d_salary_config_id")
    private Integer salaryConfigId;
}
