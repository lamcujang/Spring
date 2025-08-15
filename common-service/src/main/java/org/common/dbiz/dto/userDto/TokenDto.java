package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenDto {

    private Integer id;
    private Integer tenantId;
    private Integer orgId;
    private Integer userId;
    private String refreshToken;
//    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private String issued;
    private String expireAt;
    private String isRevoked;

    // check valid
    private String isValid;

}
