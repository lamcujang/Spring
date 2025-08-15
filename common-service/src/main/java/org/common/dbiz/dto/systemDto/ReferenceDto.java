package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.Reference}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReferenceDto implements Serializable {
    String isActive;
    Integer id;
    @NotNull
    @Size(max = 32)
    String name;
    @Size(max = 255)
    String description;
}