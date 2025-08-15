package com.dbiz.app.reportservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_declaration_resource_environment", schema = "pos")
public class TaxDeclarationResourceEnvironment extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_resource_environment_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_resource_environment_sq")
    @SequenceGenerator(name = "d_tax_declaration_resource_environment_sq", sequenceName = "d_tax_declaration_resource_environment_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "resource_type", length = 5, nullable = false)
    private String resourceType;

    @Column(name = "d_environment_fee_id", nullable = false, precision = 10)
    private Integer environmentFeeId;

    @Column(name = "quantity", precision = 20, scale = 3)
    private BigDecimal quantity;

    @Column(name = "tax_base_amount", precision = 20, scale = 2)
    private BigDecimal taxBaseAmount;

    @Column(name = "tax_amount", precision = 20, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active", length = 1)
    private String isActive;

}