package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Embeddable
@Data
public class TableKcV {


    @Column(name = "d_table_id", precision = 10)
    private Integer id;

    @Size(max = 5)
    @Column(name = "table_no", length = 5)
    private String tableNo;

    @Size(max = 32)
    @Column(name = "name_table", length = 32)
    private String nameTable;
}
