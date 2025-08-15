package com.dbiz.app.userservice.domain;

import lombok.*;

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
@Table(name = "d_user_role_access", schema = "pos")
//@IdClass(UserRoleAccess.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleAccess extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_userrole_access_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_user_role_access_sq")
    @SequenceGenerator(name = "d_user_role_access_sq", sequenceName = "d_user_role_access_sq", allocationSize = 1)
    private Integer id;


    @Column(name = "d_user_id", nullable = false)
    private Integer userId;

    @Column(name = "d_role_id", nullable = false)
    private Integer roleId;


    @Column(name="d_tenant_id")
    private Integer tenantId;

}