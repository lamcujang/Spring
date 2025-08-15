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
public class KitchenLineProductView {

    @Size(max = 255)
    @Column(name = "product_name")
    private String name;

    @Column(name = "d_product_id", precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "product_type", length = 32)
    private String productType;
    @Size(max = 32)
    @Column(name = "product_code", length = 32)
    private String productCode;

}