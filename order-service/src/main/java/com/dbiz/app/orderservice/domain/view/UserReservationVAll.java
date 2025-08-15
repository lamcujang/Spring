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
public class UserReservationVAll {

    @Column(name = "d_user_id", precision = 19)
    private Integer id;

    @Size(max = 64)
    @Column(name = "email_user", length = 64)
    private String email;

    @Column(name = "name_user", length = 64)
    private String name;
}