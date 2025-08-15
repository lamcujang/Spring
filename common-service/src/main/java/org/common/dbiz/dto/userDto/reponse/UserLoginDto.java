package org.common.dbiz.dto.userDto.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;
import org.common.dbiz.dto.tenantDto.TenantDto;
import org.common.dbiz.dto.userDto.*;

import javax.management.relation.Role;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link }
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserLoginDto implements Serializable {
    private Integer userId;

    String userName;
    String fullName;
    String phone;
    ImageDto image;
    @Email(message = "*Input must be in Email format!**")
    String email;
    Integer erpUserId;
    String birthDay;

    @JsonProperty("jwtToken")
    String accessToken;
    String refreshToken;
//    String accessTokenExp;

    String isFirstLogin;
    String gender;
    String city;
    String address;
    String genderName;
    TenantDto tenant;
    List<GetUserRoleAccessVDto> roleAccess;
    List<GetUserOrgAccessVDto> orgAccess;
    List<PosterminalOrgAccessVDto> posTerminalAccess;
    List<OrgWarehouseAccessDto>orgWarehouseAccessDto;


}