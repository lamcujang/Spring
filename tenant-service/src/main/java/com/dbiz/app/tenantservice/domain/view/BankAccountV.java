package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class BankAccountV {

    @Column(name = "d_bank_account_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "bank_name")
    private String name;


}
