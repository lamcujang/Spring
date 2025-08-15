package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.Attribute}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttributeDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    @Size(max = 32)
    String code;

    List<AttributeValueDto> attributeValues;
}