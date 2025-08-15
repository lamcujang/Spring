package org.common.dbiz.dto.integrationDto.token;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenIdempiereRespDto {

    String token;
    Integer adClientId;
    String url;
    Integer orgId;
}
