package com.dbiz.app.orderservice.domain.view;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

/**
 * Mapping for DB view
 */
@Embeddable
@Getter
@Setter
public class FloorReservationVAll {


    @Column(name = "d_floor_id", precision = 10)
    private Integer id;

    @Column(name = "display_index_floor")
    private Integer displayIndex;

    @Size(max = 255)
    @Column(name = "name_floor")
    private String name;

    @Size(max = 5)
    @Column(name = "floor_no", length = 5)
    private String floorNo;



}