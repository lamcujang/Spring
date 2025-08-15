package com.dbiz.app.reportservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_individual_industry", schema = "pos")
public class IndividualIndustry extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_individual_industry_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_individual_industry_sq")
    @SequenceGenerator(name = "d_individual_industry_sq", sequenceName = "d_individual_industry_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "d_tax_business_industry_id", nullable = false, precision = 10)
    private Integer taxBusinessIndustryId;

    @Column(name = "is_active", length = 1, nullable = false)
    private String isActive;
}