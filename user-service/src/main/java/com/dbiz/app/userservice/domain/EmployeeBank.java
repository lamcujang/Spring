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
@Table(name = "d_employee_bank", schema = "pos")
public class EmployeeBank  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_employee_bank_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_employee_bank_sq")
    @SequenceGenerator(name = "d_employee_bank_sq", sequenceName = "d_employee_bank_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Column(name = "d_employee_id")
    private Integer employeeId;

    @Size(max = 20)
    @Column(name = "account_type", length = 20)
    private String accountType;

    @Column(name = "d_bankaccount_id")
    private Integer bankAccountId;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;


}