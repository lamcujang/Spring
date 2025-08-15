package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptOtherQueryRequest extends BaseQueryRequest  implements Serializable {

    private String keyWord;
    private Integer orgId;
    private Integer tenantId;
    private Integer id;
    private String isActive;
    private String isAssign;

    private Integer userId;


}