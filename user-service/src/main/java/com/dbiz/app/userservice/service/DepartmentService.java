package com.dbiz.app.userservice.service;

import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.request.userRequest.DepartmentQueryRequest;

public interface DepartmentService extends BaseServiceGeneric<DepartmentDto, Integer, DepartmentQueryRequest>{
    Integer countEmployyesByDepartmentId(Integer departmentId);
}
