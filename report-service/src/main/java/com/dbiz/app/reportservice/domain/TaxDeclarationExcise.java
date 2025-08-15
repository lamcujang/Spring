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
@Table(name = "d_tax_declaration_excise", schema = "pos")
public class TaxDeclarationExcise extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_excise_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_excise_sq")
    @SequenceGenerator(name = "d_tax_declaration_excise_sq", sequenceName = "d_tax_declaration_excise_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_inventory_category_special_tax_id", nullable = false, precision = 10)
    private Integer inventoryCategorySpecialTaxId;

    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    @Column(name = "excise_revenue", precision = 20)
    private BigDecimal exciseRevenue;

    @Column(name = "excise_tax_amount", precision = 20)
    private BigDecimal exciseTaxAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "is_active", length = 1)
    private String isActive;
}