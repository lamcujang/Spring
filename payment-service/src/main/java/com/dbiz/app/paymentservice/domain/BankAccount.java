package com.dbiz.app.paymentservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_bankaccount", schema = "pos")
public class BankAccount  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_bankaccount_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_bankaccount_sq")
    @SequenceGenerator(name = "d_bankaccount_sq", sequenceName = "d_bankaccount_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_bank_id", nullable = false)
    private Integer bankId;

    @Size(max = 32)
    @Column(name = "account_no", nullable = false, length = 32)
    private String accountNo;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1)
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;

    @Size(max = 3)
    @Column(name = "bankaccount_type", nullable = false, length = 3)
    private String bankAccountType;

    @Size(max = 36)
    @Column(name = "d_bankaccount_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String bankAccountUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name= "branch" )
    private String branch;
}