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
public class ReferenceReservationVAll {


    @Size(max = 32)
    @Column(name = "name_reference", length = 32)
    private String nameReference;

    @Size(max = 64)
    @Column(name = "name_reference_list", length = 64)
    private String name;

    @Size(max = 15)
    @Column(name = "reference_list_value", length = 15)
    private String value;

}