package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_bonus", schema = "pos")
public class Bonus  extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_bonus_sq")
    @SequenceGenerator(name = "d_bonus_sq", sequenceName = "d_bonus_sq", allocationSize = 1)
    @Column(name = "d_bonus_id", nullable = false, precision = 10)
    private Integer id;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Size(max = 50)
    @Column(name = "bonus_group", length = 50)
    private String bonusGroup;

    @Size(max = 50)
    @Column(name = "kpi_bonus", length = 50)
    private String kpiBonus;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 1)
    @Column(name = "performance_bonus", length = 1)
    private String performanceBonus;

    @Size(max = 1)
    @Column(name = "holiday_bonus", length = 1)
    private String holidayBonus;


}