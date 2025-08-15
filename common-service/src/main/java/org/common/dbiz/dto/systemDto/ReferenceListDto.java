package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.ReferenceList}
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