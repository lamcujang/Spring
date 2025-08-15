package org.common.dbiz.request.tenantRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgQueryRequest extends BaseQueryRequest {

    String address;
    String code;
    String email;
    String name;
    String phone;
    String keyword;
    Integer id;
    Integer orgIdCurrent;
    String isActive;

}