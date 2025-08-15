package org.common.dbiz.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantQueryRequest extends BaseQueryRequest {

    String address;
    String code;
    String email;
    String name;
    String phone;
    String keyword;

}