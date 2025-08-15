package com.dbiz.app.userservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;

@Getter
@Setter
@Entity
@Table(name = "d_role", schema = "pos")
public class Role extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_role_sq")
    @SequenceGenerator(name = "d_role_sq", sequenceName = "d_role_sq", allocationSize = 1)
    @Column(name = "d_role_id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;


    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "erp_role_id")
    private Integer erpRoleId;


    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Size(max = 32)
    @Column(name = "route_function", length = 32)
    private String routeFunction;

}