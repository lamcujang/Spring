package org.common.dbiz.request.orderRequest;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateAllByIdRequest {


    private List<KitchenOrderLineList> kitchenOrderLineList;


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class KitchenOrderLineList{
         private    Integer kitchenOrderLineId;

        private  Integer productId;

        private String status;

    }


}
