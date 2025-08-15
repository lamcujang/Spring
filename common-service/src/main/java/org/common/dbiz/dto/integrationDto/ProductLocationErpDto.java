package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductLocationErpDto {
    @JsonProperty("mWarehouseId")
    Integer mWarehouseId;
    @JsonProperty("ad_Org_ID")
    Integer ad_Org_ID;
}
