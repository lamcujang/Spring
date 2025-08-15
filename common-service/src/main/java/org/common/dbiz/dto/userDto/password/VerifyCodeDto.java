package org.common.dbiz.dto.userDto.password;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class VerifyCodeDto {

    String userName;
    String email;
    String verifyCode;
}
