package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionQueryRequest extends BaseQueryRequest  implements Serializable {
        Integer id;
        private String searchKey;
        private Integer orgId;
        private String isActive;
        String promotionBasedOn;
        String promotionType;
        String isAllAssignOrg;
        String isAllBpartnerGroup;
        // cashier
        String code;
        String currentDate; // yyyy-MM-dd HH:mm:ss
        Integer bpGroupId;
}
