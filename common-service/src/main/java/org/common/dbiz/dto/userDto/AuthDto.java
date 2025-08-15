package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthDto {

    @NotBlank
    private String grantType;

    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;
}
