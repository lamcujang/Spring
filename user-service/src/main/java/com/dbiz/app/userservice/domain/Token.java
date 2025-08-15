package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_token", schema = "pos")
public class Token extends AbstractMappedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_token_sq")
    @SequenceGenerator(name = "d_token_sq", sequenceName = "d_token_sq", allocationSize = 1)
    @Column(name = "d_token_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "d_refresh_token")
    private String refreshToken;

    @Column(name = "issued")
    private Instant issued;

    @Column(name = "expire_at")
    private Instant expireAt;

    @Column(name = "is_revoked")
    private String isRevoked;

}
