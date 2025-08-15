package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_tax_business_industry", schema = "pos")
public class TaxBusinessIndustry extends AbstractMappedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_business_industry_sq")
    @SequenceGenerator(name = "d_tax_business_industry_sq", sequenceName = "d_tax_business_industry_sq", allocationSize = 1)
    @Column(name = "d_tax_business_industry_id", nullable = false, precision = 10, updatable = true)
    private Integer id;

    @Column(name = "industry_code", nullable = false, length = 32)
    private String industryCode;

    @Column(name = "industry_name", nullable = false, length = 255)
    private String industryName;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;
}