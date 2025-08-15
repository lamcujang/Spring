package org.common.dbiz.request.orderRequest;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateAllKitchenLineByIdRequest {


    private Integer orgId;
    private Integer userId;
    private List<KitchenOrderLineList> kitchenOrderLineList;


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class KitchenOrderLineList {
        private Integer kitchenOrderLineId;

        private Integer productId;

        private String status;

        private Integer posOrderId;

        private Integer cancelReasonId;

        private List<KitchenOrderLineDetailList> kitchenOrderLineDetailList;

        @NoArgsConstructor
        @AllArgsConstructor
        @Data
        public static class KitchenOrderLineDetailList {
            private Integer kitchenOrderLineId;

            private Integer productId;

            private String status;

            private Integer posOrderId;

            private Integer cancelReasonId;

            private Integer parentId;
        }
    }
}
