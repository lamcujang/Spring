package com.dbiz.app.userservice.service;

import java.util.List;

import org.common.dbiz.dto.integrationDto.UserIntDto;
import org.common.dbiz.dto.userDto.VariousUserDto;
import org.common.dbiz.dto.userDto.VariousUserParamDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.UserQueryRequest;

public interface UserService {
	
	List<UserDto> findAll();
	UserDto findById(final Integer userId);
	UserDto save(final UserDto userDto);
	UserDto update(final UserDto userDto);
	UserDto update(final Integer userId, final UserDto userDto);
	void deleteById(final Integer userId);
	UserDto findByUsername(final String username);

	UserDto findByUsernameAndDTenantId(final String username, final Integer tenantId);

	UserLoginDto findByUserNdTenantPass(final String username, final String password, final Integer tenantId);
	GlobalReponse findByIdRes(final Integer id);

	GlobalReponsePagination findAll(UserQueryRequest request);

	GlobalReponse findPosTerminalIdAccessByUserIdAndOrgId(final Integer userId, final Integer orgId);

	GlobalReponse intSave(List<UserIntDto> users);

	GlobalReponse intSaveERPNext(List<UserIntDto> users);

	GlobalReponsePagination getOrgAccess(Integer userId,Integer roleId,Integer page, Integer pageSize,String name,String searchKey, String area);

	GlobalReponse getOrgWarehouseAccess(Integer userId,Integer orgId);

	GlobalReponse getWarehouseAccess(Integer userId);

	GlobalReponse saveAll(UserDto userDto);

	GlobalReponse getByIdAndRoleId(Integer currentUserId,Integer userId,Integer roleId);

	GlobalReponse getOrgAccess(Integer userId);

	GlobalReponse registerNoToken(UserDto userDto);

	GlobalReponse getByErpUserId(Integer erpUserId);

	UserLoginDto findBySocial(UserDto userLoginDto, Integer tenantId);

	GlobalReponse saveOrgAccess(UserDto userDto);

	GlobalReponse createVariousUser(VariousUserDto userDto);
	GlobalReponsePagination getVariousUser(VariousUserParamDto userDto);

	GlobalReponse handleEx(List<UserDto> listUser);
}










