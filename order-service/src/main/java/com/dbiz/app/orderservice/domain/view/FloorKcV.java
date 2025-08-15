package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
@Embeddable
@Data
public class FloorKcV {
    @Column(name = "d_floor_id", precision = 10)
    private Integer id;

    @Size(max = 20)
    @Column(name = "floor_no", length = 20)
    private String floorNo;

    @Size(max = 255)
    @Column(name = "name_floor")
    private String nameFloor;

}
