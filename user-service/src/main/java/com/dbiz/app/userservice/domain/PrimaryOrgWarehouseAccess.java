package com.dbiz.app.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryOrgWarehouseAccess implements Serializable {
    private Integer userId;
    private Integer orgId;
    private Integer warehouseId;
    private Integer tenantId;
}
