package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_employee_grade", schema = "pos")
public class EmployeeGrade extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_employee_grade_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_employee_grade_sq")
    @SequenceGenerator(name = "d_employee_grade_sq", sequenceName = "d_employee_grade_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "job_description")
    @Type(type = "org.hibernate.type.TextType")
    private String jobDescription;

    @NotNull
    @Column(name = "permission", nullable = false)
    private String permission;

    @Column(name = "level", precision = 10)
    private BigDecimal level;

    @Column(name = "experience_required")
    @Type(type = "org.hibernate.type.TextType")
    private String experienceRequired;

    @Column(name = "base_salary_min")
    private BigDecimal baseSalaryMin;


}