package org.common.dbiz.dto.IdentityDto.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDOwnerDto {

    String username;
    String password;
    String email;
    String firstname;
    String lastname;
    String provisioningMethod;
}
