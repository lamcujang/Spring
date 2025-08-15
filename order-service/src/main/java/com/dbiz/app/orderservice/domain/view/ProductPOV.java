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
public class ProductPOV {

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Size(max = 255)
    @Column(name = "product_name")
    private String productName;

    @Column(name= "product_code")
    private String productCode;
}
