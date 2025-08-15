package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.productservice.domain.AssignOrgProduct}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignOrgProductDto implements Serializable {
    Integer id;
    Integer productId;
    Integer orgId;
    @Size(max = 1)
    String isActive;

    String name;
    String code;
    String address;
    String phone;
    String isAssign;
    String  area;
}