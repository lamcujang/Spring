package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_inventory_category_special_tax", schema = "pos")
public class InventoryCategorySpecialTax extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_inventory_category_special_tax_sq")
    @SequenceGenerator(name = "d_inventory_category_special_tax_sq", sequenceName = "d_inventory_category_special_tax_sq", allocationSize = 1)
    @Column(name = "d_inventory_category_special_tax_id", nullable = false, precision = 10, updatable = true)
    private Integer id;

    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "tax_rate", nullable = false, precision = 10)
    private BigDecimal taxRate;

    @Column(name = "unit", nullable = false, length = 15)
    private String unit;

    @Column(name = "subsection_code", precision = 10)
    private BigDecimal subsectionCode;

    @Column(name = "subsection_name", length = 255)
    private String subsectionName;

    @Column(name = "is_parent", nullable = false, length = 1)
    private String isParent;

    @Column(name = "parent_id", precision = 10)
    private Integer parentId;

    @Column(name = "grade", precision = 2)
    private Integer grade;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;
}