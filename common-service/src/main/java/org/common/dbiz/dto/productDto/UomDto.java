package org.common.dbiz.dto.productDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.Uom}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class UomDto implements Serializable {
    Integer id;
    String code;
    String name;
    String description;
    @NotNull(message = "Org id cannot be null")
     Integer orgId;
    private Integer erpUomId;


}