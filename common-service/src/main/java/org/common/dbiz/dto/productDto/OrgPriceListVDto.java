package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.view.OrgPriceListV}
 */
@NoArgsConstructor
@Data
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgPriceListVDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String name;
    @Size(max = 32)
    String code;
    @Size(max = 15)
    String phone;
    @Size(max = 100)
    String area;
    @Size(max = 1)
    String isActive;
}