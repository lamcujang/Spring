package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPosOrderLineVAllDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String name;
    @Size(max = 32)
    String code;
    @Size(max = 32)
    String productType;
    Integer productCategoryId;
    String groupType;
    String imageUrl;
}