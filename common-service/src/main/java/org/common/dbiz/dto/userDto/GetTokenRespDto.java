package org.common.dbiz.dto.userDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetTokenRespDto {

    private String accessToken;
    private String refreshToken;
//    private String accessTokenExp;

}
