package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.AttendanceRequest;
import com.dbiz.app.userservice.helper.DateHelper;
import com.dbiz.app.userservice.repository.AttendanceRequestRepository;
import com.dbiz.app.userservice.service.AttendanceRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.AttendanceRequestDto;
import org.common.dbiz.dto.userDto.nested.ConfigShiftDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.AttendanceQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceRequestServiceImpl  implements AttendanceRequestService {

    private final MessageSource messageSource;

    private final AttendanceRequestRepository attendanceRequestRepository;

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(AttendanceQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getKeyword() != null)
            parameter.add("employee_name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.AND, Param.NONE);
        if(request.getOrgId() != null)
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        if(request.getFromDate() != null && request.getToDate() != null) {
            parameter.add("created", request.getFromDate(), Param.Logical.GREATER_THAN_EQ, Param.Relational.AND, Param.NONE);
            parameter.add("created", request.getToDate(), Param.Logical.LESS_THAN_EQ, Param.Relational.AND, Param.NONE);
        }
        ResultSet rs = queryEngine.getRecords("d_attendance_request_get_v",
                parameter, request);
        List<AttendanceRequestDto> listResponse = new ArrayList<>();
        try {
         while(rs.next())
         {
        AttendanceRequestDto attendanceRequestDto = AttendanceRequestDto.builder()
                .id(rs.getInt("d_attendance_request_id"))
                .timeKeepingType(rs.getString("time_keeping_type"))
                .timeKeepingTypeName(rs.getString("time_keeping_type_name"))
                .configShiftDto(ConfigShiftDto.builder()
                        .id(rs.getInt("d_config_shift_id"))
                        .name(rs.getString("config_shift_name"))
                        .startTime(rs.getString("config_shift_start_time"))
                        .endTime(rs.getString("config_shift_end_time"))
                        .endTime(rs.getString("config_shift_end_time"))
                        .build())
                .reasonRequest(rs.getString("reason_request"))
                .status(rs.getString("status"))
                .created(DateHelper.fromInstant(rs.getTimestamp("created").toInstant()))
                .employeeDto(EmployeeDto.builder()
                        .id(rs.getInt("d_employee_id"))
                        .name(rs.getString("employee_name"))
                        .code(rs.getString("employee_code"))
                        .build())
                .orgDto(OrgDto.builder()
                        .id(rs.getInt("d_org_id"))
                        .name(rs.getString("org_name"))
                        .build()).build();
        listResponse.add(attendanceRequestDto);
         }

        }catch (Exception e)
        {
            e.printStackTrace();
             throw new RuntimeException("Error fetching attendance requests: " + e.getMessage());
        }
        Pagination pagination = queryEngine.getPagination("d_attendance_request_get_v", parameter, request);
        return GlobalReponsePagination.builder()
                .data(listResponse)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(HttpStatus.OK.value())
                .pageSize(pagination.getPageSize())
                .currentPage(pagination.getPage())
                .totalPages(pagination.getTotalPage())
                .totalItems(pagination.getTotalCount())
                .build();
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse findById(Integer integer) {
        return null;
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(AttendanceRequestDto entity) {

        AttendanceRequest entitySave;
        AttendanceRequest entityMapper  = modelMapper.map(entity,AttendanceRequest.class);
        if (entity.getId() !=null )
        {
            AttendanceRequest checkExit  = this.attendanceRequestRepository.findById(entity.getId()).orElseThrow(() -> new RuntimeException("not found"));
            modelMapper.map(entityMapper,checkExit);
            if(entity.getOrgDto() !=null)
                checkExit.setOrgId(entity.getOrgDto().getId());
            if(entity.getEmployeeDto() != null)
                checkExit.setEmployeeId(entity.getEmployeeDto().getId());
            entitySave = this.attendanceRequestRepository.save(checkExit);
        }
        else{
            entityMapper.setTenantId(0);
            entityMapper.setOrgId(entity.getOrgDto().getId());
            entityMapper.setEmployeeId(entity.getEmployeeDto().getId());
            entitySave = this.attendanceRequestRepository.save(entityMapper);
        }
        AttendanceRequestDto entityDtoResponse = modelMapper.map(entitySave, AttendanceRequestDto.class);
        if(entity.getEmployeeDto()!=null)
            entityDtoResponse.setEmployeeDto(entity.getEmployeeDto());
        if(entity.getOrgDto()!=null)
            entityDtoResponse.setOrgDto(entity.getOrgDto());
        GlobalReponse response = new GlobalReponse();
        response.setMessage("AttendanceRequest created successfully");
        response.setData(entityDtoResponse);
        response.setStatus(HttpStatus.OK.value());
        return response;

    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }
}
