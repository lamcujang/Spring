package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.userDto.UserOrgAccessDto;
import org.common.dbiz.dto.userDto.UserRoleAccessDto;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserIntDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer userId;
	
	private String userName;
	
	private String fullName;

//	@JsonProperty("d_image_id")
	private ImageDto image;
	
	private String email;
	
	private String phone;


	private String password;


	private Integer tenantId;
	private Integer erpUserId;
	private String isActive = "Y";
	private String deviceToken;
	@JsonProperty("description")
	private String description;
	private String isViewCost;
	private String isViewOther;
	private String erpUserName;

	List<UserOrgAccessIntDto> userOrgAccessDtos;
	List<UserRoleAccessIntDto> userRoleAccessDtos;







}










