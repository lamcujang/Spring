package com.dbiz.app.userservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_get_user_role_access_v", schema = "pos")
public class GetUserRoleAccessV {

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Id
    @Column(name = "d_role_id", precision = 10)
    private Integer roleId;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private BigDecimal createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private BigDecimal updatedBy;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "route_function")
    private String routeFunction;
}