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
@Table(name = "d_tax_category", schema = "pos")
public class TaxCategory extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_category_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_category_sq")
    @SequenceGenerator(name = "d_tax_category_sq", sequenceName = "d_tax_category_sq", allocationSize = 1)

    private Integer id;


    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;



    @Size(max = 36)
    @Column(name = "d_tax_category_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String taxCategoryUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "erp_tax_category_id", precision = 10)
    private Integer erpTaxCategoryId;

}