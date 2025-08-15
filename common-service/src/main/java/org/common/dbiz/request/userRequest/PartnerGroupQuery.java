package org.common.dbiz.request.userRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerGroupQuery  extends BaseQueryRequest {

    String keyword;
    String code;
    String name;
    String isCustomer;

}
