package org.common.dbiz.dto.orderDto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReferenceListDto implements Serializable {
    String isActive;
    Integer id;
    Integer referenceId;
    @NotNull
    @Size(max = 15)
    String value;
    @NotNull
    @Size(max = 64)
    String name;


}