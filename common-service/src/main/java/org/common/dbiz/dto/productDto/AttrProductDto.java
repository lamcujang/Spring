package org.common.dbiz.dto.productDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.AttributeValue}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AttrProductDto implements Serializable {
    String nameAttr;
    String valueAttr;
}