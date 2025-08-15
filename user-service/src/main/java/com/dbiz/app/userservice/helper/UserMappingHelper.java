package com.dbiz.app.userservice.helper;

import com.dbiz.app.userservice.domain.User;
import org.common.dbiz.dto.userDto.UserDto;

public interface UserMappingHelper {
	
	public static UserDto map(final User user) {
		return UserDto.builder()
				.userId(user.getUserId())
				.fullName(user.getFullName())
				.userName(user.getUserName())
				.email(user.getEmail())
				.phone(user.getPhone())
//				.dImageId(user.getDImageId())
//				.tenant(
//						TenantDto.builder()
//								.id(user.getTenant().getId())
//								.code(user.getTenant().getCode())
//								.name(user.getTenant().getName())
//								.industryId(user.getTenant().getIndustryId())
//								.build()
//				)
				.tenantId(user.getTenantId())
				.password(user.getPassword())
				.build();
	}
	
	public static User map(final UserDto userDto) {
		return User.builder()
				.userId(userDto.getUserId())
				.userName(userDto.getUserName())
				.fullName(userDto.getFullName())
				.email(userDto.getEmail())
				.phone(userDto.getPhone())
//				.dImageId(userDto.getDImageId())
//				.tenant(Tenant.builder().id(userDto
//								.getTenant().getId())
//						.name(userDto.getTenant().getName())
//						.code(userDto.getTenant().getCode())
//						.industryId(userDto.getTenant().getIndustryId())
//						.build())
				.password(userDto.getPassword())
				.build();
	}
	
	
	
}






