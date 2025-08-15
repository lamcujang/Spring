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
@Table(name = "d_posterminal_org_access_v", schema = "pos")
public class PosterminalOrgAccessV {
    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Id
    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private BigDecimal createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Size(max = 128)
    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

}