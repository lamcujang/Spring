package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "d_product_category", schema = "pos")
public class ProductCategory extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_product_category_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_product_category_sq")
    @SequenceGenerator(name = "d_product_category_sq", sequenceName = "d_product_category_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;



    @Size(max = 36)
    @Column(name = "d_product_category_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String productCategoryUu;



    @Column(name = "d_product_category_parent_id")
    private Integer productCategoryParentId;


    @Size(max = 1)
    @Column(name = "is_summary", length = 1)
    private String isSummary;

    @Column(name = "qty_product")
    private BigDecimal qtyProduct;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "erp_product_category_id", precision = 10)
    private Integer erpProductCategoryId;

    @Column(name = "index_sequence")
    private BigDecimal indexSequence;

    @Size(max = 1)
    @Column(name = "is_menu", length = 1, columnDefinition = "varchar(1) default 'Y'")
    private String isMenu;

    @Column(name="erp_product_category_name")
    private String erpProductCategoryName;

    @Column(name="d_image_id")
    private Integer imageId;

    @Column(name="d_icon_id")
    private Integer iconId;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;
}