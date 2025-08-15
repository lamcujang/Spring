package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.validation.constraints.NotNull;
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
public class PricelistDto implements Serializable {
//
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
    private String priceCateCode;
    private Integer erpPriceListId;


    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Integer[] orgIds;
    @JsonView({JsonViewPriceListDto.viewJsonFindByID.class, JsonViewPriceListDto.viewJsonFindAll.class})
    List<OrgPriceListVDto> priceListOrg;
}