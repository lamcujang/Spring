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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "d_department", schema = "pos")
public class Department extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_department_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_department_sq")
    @SequenceGenerator(name = "d_department_sq", sequenceName = "d_department_sq", allocationSize = 1)
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

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @NotNull
    @Column(name = "established_date", nullable = false)
    private LocalDate establishedDate;

    @NotNull
    @Column(name = "department_head_id", nullable = false)
    private Integer departmentHeadId;

    @Column(name = "total_employees")
    private Integer totalEmployees;

}