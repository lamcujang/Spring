package org.common.dbiz.request.tenantRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
public class IndustryQueryRequest extends BaseQueryRequest {

    String address;
    String code;
    String email;
    String name;
    String phone;
    String keyword;

}