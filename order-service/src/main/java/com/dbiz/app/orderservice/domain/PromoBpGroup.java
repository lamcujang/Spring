package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_promo_bp_group", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoBpGroup  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_promo_bp_group_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_promo_bp_group_sq")
    @SequenceGenerator(name = "d_promo_bp_group_sq", sequenceName = "d_promo_bp_group_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_promotion_id", nullable = false)
    private Integer promotionId;

    @Column(name = "d_bp_group_id", nullable = false)
    private Integer bpGroupId;
}