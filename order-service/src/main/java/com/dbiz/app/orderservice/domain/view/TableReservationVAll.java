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
public class TableReservationVAll {

    @Column(name = "d_table_id", precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "name_table", length = 32)
    private String name;

    @Size(max = 5)
    @Column(name = "table_no", length = 5)
    private String tableNo;

    @Column(name = "display_index_table")
    private Integer displayIndex;

    @Size(max = 3)
    @Column(name = "table_status", length = 3)
    private String tableStatus;


}