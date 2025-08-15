package com.dbiz.app.userservice.resource;

import com.dbiz.app.userservice.service.RoleService;
import com.dbiz.app.userservice.service.UserRolePermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.RoleDto;
import org.common.dbiz.dto.userDto.RolePermissionVDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.RoleQueryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = {"/api/v1/role"})
@Slf4j
@RequiredArgsConstructor
public class RoleResource {
	
	private final RoleService service;

	@GetMapping("/findAll")
	public GlobalReponsePagination findById(@ModelAttribute RoleQueryRequest request) {
		log.info("*** UserDto, resource; fetch user by id ***");
		return this.service.findAll(request);
	}

	@PostMapping("/save")
	public GlobalReponse save(@RequestBody RoleDto roleDto) {
		log.info("*** UserDto, resource; save user ***");
		return this.service.save(roleDto);
	}

	@GetMapping("/getRoleAccess")
	public GlobalReponsePagination getRoleAccess(@RequestParam Integer roleId , @RequestParam Integer userId,@RequestParam("page") Integer page,@RequestParam("pageSize") Integer pageSize) {
		log.info("*** UserDto, resource; fetch user by id ***");
		return this.service.getRoleAccess(roleId,userId,page,pageSize);
	}
}










