package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_declaration_vat_pit_line", schema = "pos")
public class TaxDeclarationVatPitLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_vat_pit_line_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_vat_pit_line_sq")
    @SequenceGenerator(name = "d_tax_declaration_vat_pit_line_sq", sequenceName = "d_tax_declaration_vat_pit_line_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "d_business_sector_group_id", nullable = false, precision = 10)
    private Integer businessSectorGroupId;

    @Column(name = "item_code", nullable = false, length = 32)
    private String itemCode;

    @Column(name = "vat_revenue", precision = 20)
    private BigDecimal vatRevenue;

    @Column(name = "vat_amount", precision = 20)
    private BigDecimal vatAmount;

    @Column(name = "pit_revenue", precision = 20)
    private BigDecimal pitRevenue;

    @Column(name = "pit_amount", precision = 20)
    private BigDecimal pitAmount;

    @Column(name = "is_active", length = 1)
    private String isActive;
}