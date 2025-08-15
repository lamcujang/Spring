package com.dbiz.app.orderservice.domain.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class WarehousePOV {

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Size(max = 255)
    @Column(name = "warehouse_name")
    private String warehouseName;
}
