package org.common.dbiz.dto.productDto.otherServiceDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PcERequestV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PcERequestVDto implements Serializable {
    Integer id;
    Integer orgId;
    Integer pcTerminalAccessId;
    @Size(max = 255)
    String name;
    Integer productCategoryParentId;
    @Size(max = 255)
    String parentName;
    @Size(max = 1)
    String isActive;
    Integer indexSequence;
    Integer posTerminalId;
    String isSummary;
    String code;
    String imageUrl;
    BigDecimal fromPrice;
}