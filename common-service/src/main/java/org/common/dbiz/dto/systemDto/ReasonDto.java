package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.Reason}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReasonDto implements Serializable {
    Integer id;
    @NotNull
    Integer orgId;
    @Size(max = 500)
    String name;
    @Size(max = 500)
    String type;
}