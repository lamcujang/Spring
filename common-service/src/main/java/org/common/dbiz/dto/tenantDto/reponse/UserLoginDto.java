package org.common.dbiz.dto.tenantDto.reponse;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserLoginDto implements Serializable {
    Integer dUserId;
    String userName;
    String fullName;
    String phone;
    Integer dImageId;
    String email;
    LocalDate birthDay;
}