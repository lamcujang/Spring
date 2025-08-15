package org.common.dbiz.dto.productDto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IconReqDto extends BaseQueryRequest implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private String type;
    private String imageUrl;
    private Integer tenantId;
    private Integer orgId;
    private String isActive;

}
