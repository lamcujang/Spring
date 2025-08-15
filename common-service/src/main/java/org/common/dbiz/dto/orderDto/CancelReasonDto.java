package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.CancelReason}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CancelReasonDto implements Serializable {
    Integer id;
    @Size(max = 64)
    String name;
    @Size(max = 255)
    String description;
    @NotNull(message = "Org id cannot be null")
    Integer orgId;
    @Size(max = 36)
    String dCancelReasonUu;
    String isActive;
}