package org.common.dbiz.dto.orderDto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderHistory implements Serializable {
    String created;
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class item{
        Integer productId;
        Integer posOrderId;
        String productName;
        Integer qty;
        String fullName;
        String description;
        String note;
    }
}
