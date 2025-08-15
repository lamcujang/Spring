package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 * Mapping for DB view
 */
@Embeddable
@Data
public class ProductV {

    @Column(name = "d_product_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name_product")
    private String name;


    @Size(max = 32)
    @Column(name = "code_product", length = 32)
    private String code;

    @Size(max = 32)
    @Column(name = "product_type", length = 32)
    private String productType;

    @Column(name = "d_product_category_id", precision = 10)
    private Integer productCategoryId;

    @Size(max = 45)
    @Column(name = "group_type", length = 45)
    private String groupType;

}