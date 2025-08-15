package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class FloorV {
    @Column(name = "d_floor_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "floor_name")
    private String name;

}
