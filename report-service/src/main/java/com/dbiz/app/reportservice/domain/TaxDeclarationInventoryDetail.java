package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_declaration_inventory_detail", schema = "pos")
public class TaxDeclarationInventoryDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_inventory_detail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_inventory_detail_sq")
    @SequenceGenerator(name = "d_tax_declaration_inventory_detail_sq", sequenceName = "d_tax_declaration_inventory_detail_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    private Integer taxDeclarationIndividualId;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "unit_name", length = 50)
    private String unitName;

    @Column(name = "begin_quantity", precision = 20, scale = 4)
    private BigDecimal beginQuantity;

    @Column(name = "begin_amount", precision = 20, scale = 2)
    private BigDecimal beginAmount;

    @Column(name = "import_quantity", precision = 20, scale = 4)
    private BigDecimal importQuantity;

    @Column(name = "import_amount", precision = 20, scale = 2)
    private BigDecimal importAmount;

    @Column(name = "export_quantity", precision = 20, scale = 4)
    private BigDecimal exportQuantity;

    @Column(name = "export_amount", precision = 20, scale = 2)
    private BigDecimal exportAmount;

    @Column(name = "remain_quantity", precision = 20, scale = 4)
    private BigDecimal remainQuantity;

    @Column(name = "remain_amount", precision = 20, scale = 2)
    private BigDecimal remainAmount;

    @Column(name = "begin_unit_price", precision = 20, scale = 2)
    private BigDecimal beginUnitPrice;

    @Column(name = "import_unit_price", precision = 20, scale = 2)
    private BigDecimal importUnitPrice;

    @Column(name = "export_unit_price", precision = 20, scale = 2)
    private BigDecimal exportUnitPrice;

    @Column(name = "remain_unit_price", precision = 20, scale = 2)
    private BigDecimal remainUnitPrice;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;
}