package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.EmployeeAttendance;
import com.dbiz.app.userservice.repository.EmployeeAttendanceRepository;
import com.dbiz.app.userservice.service.EmployeeAttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.EmployeeAttendanceDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeAttendanceQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeAttendanceServiceImpl implements EmployeeAttendanceService  {
    private final EmployeeAttendanceRepository employeeAttendanceRepository;

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;


    private final QueryEngine queryEngine;


    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(EmployeeAttendanceQueryRequest request) {
        GlobalReponsePagination response = new GlobalReponsePagination();
        Parameter parameter = new Parameter();
        if(request.getOrgId() != null)
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
        if(request.getId() != null)
            parameter.add("d_employee_attendance_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("d_employee_attendance_get_v",
                parameter, request);
        List<EmployeeAttendanceDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                EmployeeAttendanceDto dto = EmployeeAttendanceDto.builder()
                                .id(rs.getInt("d_employee_attendance_id"))
                                .attendanceDate(DateHelper.fromLocalDate(rs.getObject("d_attendance_date", LocalDate.class)))
                                        .checkinTime(DateHelper.formatLocalTimeToString(rs.getObject("d_checkin_time", LocalTime.class)))
                                                .build();
                    listResponse.add(dto);
            }
        } catch (Exception e) {
            log.error("Error fetching employee attendance data: ", e);
        }
        Pagination pagination = queryEngine.getPagination("d_employee_attendance_get_v", parameter, request);
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
    public GlobalReponse save(EmployeeAttendanceDto entity) {
        EmployeeAttendance  data = modelMapper.map(entity, EmployeeAttendance.class);
        EmployeeAttendance dataReturn ;
        GlobalReponse dataResponse = new GlobalReponse();
        if(data.getId() != null )
        {
            EmployeeAttendance employeeAttCheck = this.employeeAttendanceRepository.findById(data.getId() ).orElseThrow(() -> new RuntimeException("Employee Attendance not found"));
            modelMapper.map(entity,employeeAttCheck);
            handleSave(entity, employeeAttCheck);
            dataReturn = this.employeeAttendanceRepository.save(employeeAttCheck);
            dataResponse.setStatus(HttpStatus.OK.value());
            dataResponse.setMessage( messageSource.getMessage("message.update.success", null, LocaleContextHolder.getLocale()));
        }else{
            handleSave(entity, data);
            dataReturn = this.employeeAttendanceRepository.save(data);
            dataResponse.setStatus(HttpStatus.CREATED.value());
            dataResponse.setMessage( messageSource.getMessage("message.create.success", null, LocaleContextHolder.getLocale()));
        }
        EmployeeAttendanceDto dto = modelMapper.map(dataReturn, EmployeeAttendanceDto.class);
        dto.setOrgDto(entity.getOrgDto() != null ? entity.getOrgDto() : null);
        dto.setEmployeeDto(entity.getEmployeeDto() != null ? entity.getEmployeeDto() : null);
        dto.setApprovedByDto(entity.getApprovedByDto() != null ? entity.getApprovedByDto() : null);
        dto.setAttendanceDate( DateHelper.fromLocalDate(dataReturn.getAttendanceDate()) );
        dto.setCheckinTime( DateHelper.formatLocalTimeToString(dataReturn.getCheckinTime()) );
        dataResponse.setData(dto);

        return null;
    }

    /**
     *
     * @param integer
     * @return
     */
    @Override
    public GlobalReponse deleteById(Integer integer) {
        EmployeeAttendance employeeAttendance = this.employeeAttendanceRepository.findById(integer).orElseThrow(() -> new RuntimeException("Employee Attendance not found"));
        this.employeeAttendanceRepository.delete(employeeAttendance);
        GlobalReponse response = new GlobalReponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(messageSource.getMessage("message.delete.success", null, LocaleContextHolder.getLocale()));
        response.setData(null);
        return response;
    }

    private void handleSave(EmployeeAttendanceDto dto , EmployeeAttendance entity)
    {
        if(dto.getOrgDto() != null)
            entity.setId(dto.getOrgDto().getId());
        if(dto.getEmployeeDto() != null)
            entity.setEmployeeId(dto.getEmployeeDto().getId());
        if(dto.getApprovedByDto() != null)
            entity.setApprovedBy(dto.getApprovedByDto().getId());
        if(dto.getAttendanceDate() !=null)
            entity.setAttendanceDate(DateHelper.toLocalDate(dto.getAttendanceDate() ));
        if(dto.getCheckinTime() !=null)
            entity.setCheckinTime(DateHelper.parseStringToLocalTime(dto.getCheckinTime() ));
    }

}
