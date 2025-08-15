package org.common.dbiz.dto.reportDto.material;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class MaterialLedger {
    private Integer tenantId;
    private String name;
    private String address;
    private List<ProductLedger> data;
}
