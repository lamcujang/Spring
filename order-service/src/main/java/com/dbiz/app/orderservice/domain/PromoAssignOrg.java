package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_promo_assign_org", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoAssignOrg extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_promo_assign_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_promo_assign_org_sq")
    @SequenceGenerator(name = "d_promo_assign_org_sq", sequenceName = "d_promo_assign_org_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_promotion_id", nullable = false)
    private Integer promotionId;

}