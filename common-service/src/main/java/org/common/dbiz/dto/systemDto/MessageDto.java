package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.systemservice.domain.Message}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    @Size(max = 512)
    String value;
    String msgText;
}