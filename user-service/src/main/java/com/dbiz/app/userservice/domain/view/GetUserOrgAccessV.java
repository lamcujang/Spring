package com.dbiz.app.userservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
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
    @Table(name = "d_get_user_org_access_v", schema = "pos")
public class GetUserOrgAccessV {

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;
    @Id
    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;


    @Column(name = "is_pos_mng")
    private String isPosMng;

}