package com.dbiz.app.tenantservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;


@Embeddable
@Data
public class WarehouseV {

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer id;

    @Size(max = 255)
    @Column(name = "warehouse_name")
    private String name;
}
