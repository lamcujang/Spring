package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class DoctypeV {

    @Column(name = "d_doctype_id", precision = 10)
    private Integer id;

    @Size(max = 128)
    @Column(name = "doctype_name", length = 128)
    private String name;
}
