package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductVDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String name;
    @Size(max = 32)
    String code;
    @Size(max = 32)
    String productType;
}