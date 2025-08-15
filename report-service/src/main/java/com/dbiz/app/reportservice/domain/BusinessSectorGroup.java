package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_business_sector_group", schema = "pos")
public class BusinessSectorGroup extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_business_sector_group_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_business_sector_group_sq")
    @SequenceGenerator(name = "d_business_sector_group_sq", sequenceName = "d_business_sector_group_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 36)
    @Column(name = "d_business_sector_group_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String businessSectorGroupUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "description")
    private String description;

    @Column(name = "indicator_code")
    private String indicatorCode;

    @Column(name = "vat_tax_rate")
    private BigDecimal vatTaxRate;

    @Column(name = "pit_tax_rate")
    private BigDecimal pitTaxRate;
}