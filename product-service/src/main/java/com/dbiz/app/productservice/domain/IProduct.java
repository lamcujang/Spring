package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "i_product", schema = "pos") 
public class IProduct extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "i_product_sq")
    @SequenceGenerator(name = "i_product_sq", sequenceName = "i_product_sq", allocationSize = 1)
    @Column(name = "i_product_id", nullable = false, precision = 10)
    private Integer id;
  
    @Column(name = "d_product_category_id", precision = 10)
    private Integer productCategoryId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "qrcode")
    private String qrcode;

    @Column(name = "saleprice", precision = 10, scale = 2)
    private Integer saleprice;

    @Column(name = "costprice", precision = 10, scale = 2)
    private Integer costprice;

    @Column(name = "d_uom_id", precision = 10)
    private Integer dUomId;

    @Column(name = "on_hand", precision = 10, scale = 2)
    private Integer onHand;

    @Column(name = "is_purchased", length = 1)
    private String isPurchased;

    @Column(name = "d_image_id", precision = 10)
    private Integer dImageId;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "attribute1")
    private String attribute1;

    @Column(name = "attribute2")
    private String attribute2;

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

    @Size(max = 1)
    @Column(name = "is_topping", length = 1)
    private String isTopping;

    @Column(name = "min_on_hand")
    private Integer minOnHand;

    @Column(name = "max_on_hand")
    private Integer maxOnHand;

    @Column(name = "d_tax_id")
    private Integer taxId;

    @Size(max = 45)
    @Column(name = "group_type", length = 45)
    private String groupType;

    @Column(name = "d_product_parent_id")
    private Integer productParentId;

    @Lob
    @Column(name = "d_locator_id")
    private String locatorId;

    @Column(name = "qty_conversion")
    private Integer qtyConversion;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "erp_product_id", precision = 10)
    private Integer erpProductId;

    @Size(max = 200)
    @Column(name = "brand", length = 200)
    private String brand;

    @Column(name = "qty")
    private Integer qty;

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
    private Integer cookingTime;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @Column(name = "error_message")
    @Type(type = "org.hibernate.type.TextType")
    private String errorMessage;
    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}