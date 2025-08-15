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
public class UserPOV {
    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Size(max = 128)
    @Column(name = "full_name", length = 128)
    private String fullName;
}
