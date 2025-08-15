package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
@Data
public class BankAccountVisaV  {

    @Column(name = "d_bank_account_visa_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "bank_visa_name")
    private String name;
}
