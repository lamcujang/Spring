package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_tax", schema = "pos")
public class Tax extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_sq")
    @SequenceGenerator(name = "d_tax_sq", sequenceName = "d_tax_sq", allocationSize = 1)
    private Integer id;


    @Size(max = 64)
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "d_tax_category_id", nullable = false)
    private Integer taxCategoryId;

    @Column(name = "tax_rate", nullable = false, precision = 10)
    private BigDecimal taxRate;

    @Size(max = 1)
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;

    @Size(max = 1)
    @Column(name = "is_saletax", length = 1)
    private String isSaletax;



    @Size(max = 36)
    @Column(name = "d_tax_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String taxUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "erp_tax_id", precision = 10)
    private Integer erpTaxId;

}