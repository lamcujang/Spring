package com.dbiz.app.productservice.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "d_business_sector", schema = "pos")
public class BusinessSector implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_business_sector_sq")
    @SequenceGenerator(name = "d_business_sector_sq", sequenceName = "pos.d_business_sector_sq", allocationSize = 1)
    @Column(name = "d_business_sector_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_business_sector_group_id", precision = 10)
    private Integer businessSectorGroupId;

    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false, precision = 10)
    private Integer tenantId;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "name", length = 1024)
    private String name;

    @Column(name = "vat_rate", precision = 5, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "pit_rate", precision = 5, scale = 2)
    private BigDecimal pitRate;

    @Column(name = "d_business_sector_uu", length = 36, columnDefinition = "UUID DEFAULT pos.uuid_generate_v4()", insertable = false, updatable = false)
    private String businessSectorUu;
}
