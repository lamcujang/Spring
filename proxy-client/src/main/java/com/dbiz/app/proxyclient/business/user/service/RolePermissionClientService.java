package com.dbiz.app.proxyclient.business.user.service;

import com.dbiz.app.proxyclient.business.user.model.UserDto;
import com.dbiz.app.proxyclient.business.user.queryRequest.UserQueryRequest;
import org.common.dbiz.dto.userDto.RolePermissionVDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "USER-SERVICE", contextId = "rolePermissionClientService", path = "/user-service/api/v1/rolePermission", decode404 = true)
public interface RolePermissionClientService {

	@GetMapping("")
	ResponseEntity<GlobalReponse> findById(@RequestParam(value = "roleId",defaultValue =  "0") Integer id);

	@PostMapping("/save")
	ResponseEntity<GlobalReponse> save(@RequestBody RolePermissionVDto param);
}










