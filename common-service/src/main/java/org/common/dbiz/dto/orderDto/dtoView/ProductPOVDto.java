package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductPOVDto implements Serializable {
    Integer productId;
    String productName;
    String productCode;
}