package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_employee_contact", schema = "pos")
public class EmployeeContact  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_employee_contact_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_employee_contact_sq")
    @SequenceGenerator(name = "d_employee_contact_sq", sequenceName = "d_employee_contact_sq", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "d_employee_id")
    private Integer employeeId;

    @Size(max = 500)
    @Column(name = "full_name", length = 500)
    private String fullName;
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;
    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 50)
    @Column(name = "relationship", length = 50)
    private String relationship;

    @Size(max = 50)
    @Column(name = "contact_type", length = 50)
    private String contactType;

    @Size(max = 500)
    @Column(name = "address", length = 500)
    private String address;

}