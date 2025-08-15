package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.AttributeValue}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttributeValueDto implements Serializable {
    Integer tenantId;
    Integer orgId;
    @Size(max = 1)
    String isActive;
    Integer id;
    @Size(max = 32)
    String value;
    @Size(max = 32)
    String name;
    Integer attributeId;
}