package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_promotion", schema = "pos")
public class Promotion extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_promotion_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_promotion_sq")
    @SequenceGenerator(name = "d_promotion_sq", sequenceName = "d_promotion_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_user_id", nullable = false)
    private Integer userId;


    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "promotion_based_on", nullable = false)
    private String promotionBasedOn;

    @Column(name = "promotion_type", nullable = false)
    private String promotionType;

    @Size(max = 1)
    @Column(name = "is_apply_birthday", nullable = false, length = 1)
    private String isApplyBirthday;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_warn_if_used", nullable = false, length = 1)
    private String isWarnIfUsed;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "to_date")
    private Instant toDate;

    @Column(name = "by_date")
    private String byDate;

    @Column(name = "by_month")
    private String byMonth;

    @Column(name = "excluded_date")
    private String excludedDate;

    @Column(name = "is_all_org")
    private String isAllOrg;

    @Column(name = "is_all_bpartner")
    private String isAllBpartner;

    @Column(name = "is_scale_with_qty")
    private String isScaleWithQty;

}