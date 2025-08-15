package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.RolePermissionVDto;
import org.common.dbiz.payload.GlobalReponse;

import java.util.List;

public interface UserRolePermissionService {
    GlobalReponse getRolePermission(Integer roleId);

    GlobalReponse save(RolePermissionVDto rolePermissionVDtos);
}
