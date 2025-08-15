package org.common.dbiz.dto.integrationDto.token;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenERPNextRespDto {

    String token;
    String url;
    String apiKey;
    String apiSecret;
}
