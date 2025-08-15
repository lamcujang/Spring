package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.common.dbiz.dto.userDto.UserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.dbiz.app.integrationservice.domain.IntegrationHistory}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntegrationHistoryDto implements Serializable {
    String isActive;
    Integer id;
    Integer userId;
    Integer orgId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    String intDate;
    @NotNull
    @Size(max = 3)
    String intType;
    @NotNull
    @Size(max = 3)
    String intFlow;
    @NotNull
    @Size(max = 3)
    String intStatus;

    String payload;
    String response;
    UserDto user;
    private String description;
    private String integrationType;


}