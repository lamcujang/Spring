package com.dbiz.app.userservice.service;

import com.dbiz.app.userservice.domain.Role;
import org.common.dbiz.dto.userDto.RoleDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.RoleQueryRequest;

public interface RoleService {
    GlobalReponsePagination findAll(RoleQueryRequest roleQueryRequest);

    GlobalReponse save(RoleDto roleDto);

    GlobalReponsePagination getRoleAccess(Integer roleId, Integer userId, Integer page, Integer pageSize);
}
