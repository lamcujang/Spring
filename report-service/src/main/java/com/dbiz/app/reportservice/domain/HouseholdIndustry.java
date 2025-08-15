package com.dbiz.app.reportservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_household_industry", schema = "pos")
public class HouseholdIndustry extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_household_industry_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_household_industry_sq")
    @SequenceGenerator(name = "d_household_industry_sq", sequenceName = "d_household_industry_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_household_profile_id", nullable = false, precision = 10)
    private Integer taxHouseholdProfileId;

    @Column(name = "d_tax_business_industry_id", nullable = false, precision = 10)
    private Integer taxBusinessIndustryId;

    @Column(name = "is_active", length = 1, nullable = false)
    private String isActive;
}