package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.springframework.web.bind.annotation.Mapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_product", schema = "pos")
@EqualsAndHashCode(callSuper = true, exclude = {"productCategory" , "uom","image"})
public class Product extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_product_sq")
    @SequenceGenerator(name = "d_product_sq", sequenceName = "d_product_sq", allocationSize = 1)
    @Column(name = "d_product_id", nullable = false, precision = 10,updatable = true)
    private Integer id;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "d_product_category_id", referencedColumnName = "d_product_category_id", nullable = false)
//    private ProductCategory productCategory;
    @Column(name = "d_product_category_id")
    private Integer productCategoryId;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 36)
    @Column(name = "qrcode", length = 36)
    private String qrcode;

    @Column(name = "saleprice", precision = 10, scale = 2)
    private BigDecimal saleprice;

    @Column(name = "costprice", precision = 10, scale = 2)
    private BigDecimal costprice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "d_uom_id", referencedColumnName = "d_uom_id", nullable = false)
    private Uom uom;

    @Column(name = "on_hand", precision = 10, scale = 2)
    private BigDecimal onHand;

    @Size(max = 1)
    @Column(name = "is_purchased", length = 1)
    private String isPurchased;

//    @ManyToOne(fetch = FetchType.EAGER)
    @ManyToOne(fetch = FetchType.EAGER)// dung CascadeType.ALL de khi luu va xoa image tu xoa theo
    @JoinColumn(name = "d_image_id", referencedColumnName = "d_image_id", nullable = false)
    private Image image;

    @Column(name = "product_type")
//    @Enumerated(EnumType.STRING)
    private String productType;

    @Size(max = 255)
    @Column(name = "attribute1")
    private String attribute1;

    @Size(max = 255)
    @Column(name = "attribute2")
    private String attribute2;

    @Size(max = 255)
    @Column(name = "attribute3")
    private String attribute3;

    @Size(max = 255)
    @Column(name = "attribute4")
    private String attribute4;

    @Size(max = 255)
    @Column(name = "attribute5")
    private String attribute5;

    @Size(max = 255)
    @Column(name = "attribute6")
    private String attribute6;

    @Size(max = 255)
    @Column(name = "attribute7")
    private String attribute7;

    @Size(max = 255)
    @Column(name = "attribute8")
    private String attribute8;

    @Size(max = 255)
    @Column(name = "attribute9")
    private String attribute9;

    @Size(max = 255)
    @Column(name = "attribute10")
    private String attribute10;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_product_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String productUu;

    @Size(max = 1)
    @Column(name = "is_topping", nullable = false, length = 1)
    private String isTopping;


    @Column(name = "min_on_hand")
    private BigDecimal minOnHand;

    @Column(name = "max_on_hand")
    private BigDecimal maxOnHand;


    @Column(name = "d_tax_id")
    private Integer taxId;

    @Column(name = "d_business_sector_id")
    private Integer businessSectorId;

    @Column(name = "group_type")
//    @Enumerated(EnumType.STRING)
    private String groupType;

    @Column(name = "d_product_parent_id", precision = 10)
    private Integer productParentId;

    @Lob
    @Column(name = "d_locator_id")
    private String locatorId;

    @Column(name = "qty_conversion")
    private BigDecimal qtyConversion;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "erp_product_id", precision = 10)
    private Integer erpProductId;

    @Size(max = 200)
    @Column(name = "brand", length = 200)
    private String brand;

    @Column(name = "qty")
    private BigDecimal qty;


// view product theo org
    @OneToMany(mappedBy = "productId", fetch = FetchType.LAZY)
    private List<AssignOrgProduct> assignOrgProducts;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 1)
    @Column(name = "is_sales", length = 1)
    private String isSales;


    @Size(max = 1)
    @Column(name = "is_stocked", length = 1)
    private String isStocked;

    @Size(max = 1)
    @Column(name = "auxiliar", length = 1)
    private String auxiliar;

    @Size(max = 1)
    @Column(name = "is_show_pos", length = 1)
    private String isShowPos;

    @Column(name = "cooking_time")
    private BigDecimal cookingTime;

    @Column(name = "preparation_time")
    private BigDecimal preparationTime;

    @Column(name = "sku")
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "d_brand_id")
    private Integer brandId;

    @Column(name = "p_height")
    private BigDecimal height;

    @Column(name = "p_width")
    private BigDecimal width;

    @Column(name = "p_length")
    private BigDecimal length;

    @Column(name = "is_expiry_warning")
    private String isExpiryWarning;

    @Column(name = "expiry_warning_day")
    private Integer expiryWarningDay;

    @Column(name = "is_allow_com_export")

    private String isAllowComExport;

    @Column(name = "weight_uom")
    private String weightUom;

    @Column(name = "spec_uom")
    private String specUom;

    @Column(name = "d_inventory_category_special_tax_id")
    private Integer inventoryCategorySpecialTaxId;

    @Column(name = "declared_tax_reduction")
    private String declaredTaxReduction;

}