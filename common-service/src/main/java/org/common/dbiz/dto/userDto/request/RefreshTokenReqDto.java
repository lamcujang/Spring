package org.common.dbiz.dto.userDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshTokenReqDto {

    private String accessToken;
    private String refreshToken;

}
