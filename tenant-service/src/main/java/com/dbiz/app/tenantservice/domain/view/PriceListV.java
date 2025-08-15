package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class PriceListV {

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer id;

    @Size(max = 64)
    @Column(name = "pricelist_name", length = 64)
    private String name;
}
