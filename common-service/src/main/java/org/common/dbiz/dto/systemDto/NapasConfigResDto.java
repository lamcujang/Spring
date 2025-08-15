package org.common.dbiz.dto.systemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NapasConfigResDto {

    String url;
    String clientId;
    String clientSecret;
    String grantType;
    String napasCode;
    String masterMerchantCode;

}
