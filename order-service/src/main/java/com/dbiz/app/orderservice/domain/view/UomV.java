package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class UomV {

    @Column(name = "d_uom_id", precision = 10)
    private Integer id;


    @Size(max = 15)
    @Column(name = "name_uom", length = 15)
    private String name;

}
