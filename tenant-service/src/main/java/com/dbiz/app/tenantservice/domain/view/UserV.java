package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class UserV {

    @Column(name = "d_user_id", precision = 10)
    private BigDecimal userId;

    @Size(max = 128)
    @Column(name = "full_name", length = 128)
    private String fullName;

    @Size(max = 64)
    @Column(name = "user_name", length = 64)
    private String userName;
}
