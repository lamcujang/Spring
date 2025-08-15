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
public class CustomerReservationVAll {


    @Column(name = "d_customer_id", precision = 10)
    private Integer id;

    @Size(max = 64)
    @Column(name = "email_customer", length = 64)
    private String email;

    @Size(max = 255)
    @Column(name = "address_customer")
    private String address;

    @Size(max = 255)
    @Column(name = "name_customer")
    private String name;
}