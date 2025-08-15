package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_config_shift_employee", schema = "pos")
public class ConfigShiftEmployee extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_config_shift_employee_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_config_shift_employee_sq")
    @SequenceGenerator(name = "d_config_shift_employee_sq", sequenceName = "d_config_shift_employee_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Column(name = "d_config_shift_id")
    private Integer configShiftId;

    @Column(name = "d_employee_id")
    private Integer employeeId;

    @Column(name="is_active")
    private String isActive;
    }