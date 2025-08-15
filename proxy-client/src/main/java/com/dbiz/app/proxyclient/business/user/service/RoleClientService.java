package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.business.user.model.UserDto;
import com.dbiz.app.proxyclient.business.user.queryRequest.UserQueryRequest;
import org.common.dbiz.dto.userDto.RoleDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.RoleQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FeignClient(name = "USER-SERVICE", contextId = "roleClientService", path = "/user-service/api/v1/role", decode404 = true)
public interface RoleClientService {
	
 	@GetMapping("/findAll")
	GlobalReponsePagination findAll(@SpringQueryMap RoleQueryRequest request);

	@PostMapping("/save")
	GlobalReponse save(@RequestBody RoleDto userDto);

	@GetMapping("/getRoleAccess")
	GlobalReponsePagination getRoleAccess(@RequestParam Integer roleId , @RequestParam Integer userId,@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize);

}










