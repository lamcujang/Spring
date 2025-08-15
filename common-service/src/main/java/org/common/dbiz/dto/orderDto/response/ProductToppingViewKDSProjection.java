package org.common.dbiz.dto.orderDto.response;

public interface ProductToppingViewKDSProjection {
    Integer getId();
    String getCode();
    String getName();
    Integer getQty();
    Integer getKitchenOrderLineId();
    Integer getParentId();
}
