package org.common.dbiz.dto.orderDto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReturnReasonDto {
    Integer id;
    @Size(max = 64)
    String name;
    @Size(max = 255)
    String description;
    @NotNull(message = "Org id cannot be null")
    Integer orgId;
    @Size(max = 36)
    String dReturnReasonUu;

    String isActive;
}
