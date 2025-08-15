package com.dbiz.app.userservice.resource;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.AuditInfo;
import com.dbiz.app.userservice.helper.UserMapper;
import com.dbiz.app.userservice.service.UserRolePermissionService;
import com.dbiz.app.userservice.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.RolePermissionVDto;
import org.common.dbiz.dto.userDto.UserDto;
import org.common.dbiz.dto.userDto.jsonView.JsonViewUserDto;
import org.common.dbiz.dto.userDto.reponse.UserLoginDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.UserQueryRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/rolePermission"})
@Slf4j
@RequiredArgsConstructor
public class UserRolePermissionResource {
	
	private final UserRolePermissionService service;

	@GetMapping("")
	public GlobalReponse findById(@RequestParam("roleId") Integer id) {
		log.info("*** UserDto, resource; fetch user by id ***");
		return this.service.getRolePermission(id);
	}


	@PostMapping("/save")
	public GlobalReponse save(@RequestBody  RolePermissionVDto params) {
		log.info("*** UserDto, resource; save user ***");
		return this.service.save(params);
	}
}










