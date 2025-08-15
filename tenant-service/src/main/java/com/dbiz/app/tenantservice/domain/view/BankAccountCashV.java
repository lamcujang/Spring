package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class BankAccountCashV {

    @Column(name = "d_bank_account_cash_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "bank_cash_name")
    private String name;
}
