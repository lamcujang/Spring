package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link PriceList}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceListIntDto implements Serializable {

    @Size(max = 1)
    String isActive;
    Integer id;

    @Size(max = 64)
    String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String fromDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String toDate;
    @Size(max = 1)
    String isSaleprice;
    Integer orgId;
    String generalPriceList;
    Integer pricePrecision;
    private String isDefault;
    private Integer erpPriceListId;
    String maxDate = "N";

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Integer[] orgIds;
    @JsonView({JsonViewPriceListDto.viewJsonFindByID.class, JsonViewPriceListDto.viewJsonFindAll.class})
    List<OrgPriceListVDto> priceListOrg;

    List<PriceListProductDto>listPriceProduct;
}