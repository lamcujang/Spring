package org.common.dbiz.dto.userDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.ImageDto;
import org.common.dbiz.dto.tenantDto.CreateOrgDto;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

//	private Integer id;
	
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
	private String googleId;
	private String facebookId;
	private String zaloId;
	private String tiktokId;
	private String appleId;
	private String iServerId;
	private String isTenantAdmin;
	private String industryCode;
	private String numberOfTables;
	private String birthDay;
	private String gender;
	private String address;
	private String city;
	private String genderName;
	private List<CreateOrgDto> orgs;
	private String wards;

	Integer orgId; // sai cho luu org role admin
	List<UserOrgAccessDto> userOrgAccessDtos;
	List<UserRoleAccessDto> userRoleAccessDtos;
	List<OrgWarehouseAccessDto> orgWarehouseAccessDtos;






}










