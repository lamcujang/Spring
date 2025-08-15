package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class TableV {


    @Column(name = "d_table_id", precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "table_name", length = 32)
    private String name;

}
