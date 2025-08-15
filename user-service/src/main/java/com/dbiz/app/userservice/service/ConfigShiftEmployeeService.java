package com.dbiz.app.userservice.service;

import com.dbiz.app.userservice.domain.ConfigShift;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.request.userRequest.ConfigShiftEmployeeQueryRequest;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;

import java.util.List;

public interface ConfigShiftEmployeeService extends BaseServiceGeneric<ConfigShiftEmployeeDto, Integer, ConfigShiftEmployeeQueryRequest>{

    public List<ConfigShiftEmployeeDto> saveAll(List<ConfigShiftEmployeeDto> params, ConfigShift configShift);
}
