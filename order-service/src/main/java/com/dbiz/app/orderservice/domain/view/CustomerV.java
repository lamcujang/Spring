package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class CustomerV {
    @Column(name = "d_customer_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String name;

    @Size(max = 15)
    @Column(name = "phone1", length = 15)
    private String phone1;

    @Column(name = "discount")
    private BigDecimal discount;

}
