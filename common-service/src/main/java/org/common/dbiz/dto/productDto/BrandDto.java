package org.common.dbiz.dto.productDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BrandDto implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private Integer tenantId;
    private Integer orgId;
    private String isActive;
}
