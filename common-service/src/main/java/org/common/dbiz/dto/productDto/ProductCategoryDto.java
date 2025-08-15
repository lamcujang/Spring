package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.common.dbiz.dto.PcTerminalAccessDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.ProductCategory}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductCategoryDto implements Serializable {
    Integer id;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    Integer orgId;
    String isMenu;
    String isCommodities;
    String isGroup;
    String isDefault;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    Integer productCategoryParentId;
    ProductCategoryDto productCategoryParent;
    String isSummary;
    private String isActive;
    BigDecimal qtyProduct;
    Integer erpProductCategoryId;
    private BigDecimal indexSequence;
    private String erpProductCategoryName;
    String erpParentProductCat;
    ImageDto imageDto;
    IconDto iconDto;
    List<PcTerminalAccessDto> pcTerminalAccesses;
}