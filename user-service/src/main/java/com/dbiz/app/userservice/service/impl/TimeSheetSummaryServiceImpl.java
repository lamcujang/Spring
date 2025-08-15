package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.helper.RequestParamsUtils;
import com.dbiz.app.userservice.domain.LeaveApplication;
import com.dbiz.app.userservice.domain.OverTimeLog;
import com.dbiz.app.userservice.domain.TimeSheetSummary;
import com.dbiz.app.userservice.repository.LeaveApplicationRepository;
import com.dbiz.app.userservice.repository.OverTimeLogRepository;
import com.dbiz.app.userservice.repository.TimeSheetSummaryRepository;
import com.dbiz.app.userservice.service.TimeSheetSummaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.common.dbiz.dto.userDto.*;
import org.common.dbiz.dto.userDto.request.TimeSheetSummaryRequest;
import org.common.dbiz.exception.PosException;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeSheetSummaryServiceImpl implements TimeSheetSummaryService {
    TimeSheetSummaryRepository timeSheetRepository;
    LeaveApplicationRepository leaveApplicationRepository;
    OverTimeLogRepository overTimeLogRepository;

    ModelMapper modelMapper;
    MessageSource messageSource;
    RequestParamsUtils requestParamsUtils;
    QueryEngine queryEngine;

    @Override
    public GlobalReponsePagination findAll(TimeSheetSummaryRequest req) {
//        Pageable page = requestParamsUtils.getPageRequest(request);
//
//        Specification<TimeSheetSummary> spec = TimeSheetSummarySpecification.getSpecification(request);
//
//        Page<TimeSheetSummary> entityList = timeSheetRepository.findAll(spec, page);
//        List<TimeSheetSummaryDto> listData = new ArrayList<>();
//        if(entityList.hasContent()) {
//            entityList.getContent().forEach(entity -> {
//                TimeSheetSummaryDto dto = modelMapper.map(entity, TimeSheetSummaryDto.class);
//                listData.add(dto);
//            });
//        }
//
//        return GlobalReponsePagination.builder()
//                .pageSize(entityList.getNumber())
//                .currentPage(entityList.getNumber())
//                .data(listData)
//                .totalPages(entityList.getTotalPages())
//                .totalItems(entityList.getTotalElements())
//                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
//                .build();


        Parameter parameter = new Parameter();
        if(req.getMonth() != null){
            String monthStr = req.getMonth(); // "2025-08"
            YearMonth yearMonth = YearMonth.parse(monthStr);

            String start = yearMonth.atDay(1).toString(); // "2025-08-01"
            String end = yearMonth.atEndOfMonth().toString() + " 23:59:59"; // "2025-08-31 23:59:59"
            parameter.add("created", Param.getBetweenParam(start, end), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        }

        parameter.add("employee_type", req.getEmployeeType(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_department_id ", req.getDepartmentId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_org_id", req.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("employee_name", req.getEmployeeKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.START);
        parameter.add("employee_code", req.getEmployeeKeyword(), Param.Logical.LIKE, Param.Relational.NONE, Param.END);
        ResultSet rs = queryEngine.getRecords("pos.d_h_timesheet_summary_v", parameter, req);
        try {
            List<HTimeSheetSummaryDto> data = new ArrayList<>();
            while (rs.next()) {
                HTimeSheetSummaryDto hTimeSheetSummaryDto = HTimeSheetSummaryDto.builder()
                        .actualWorkingHours(rs.getBigDecimal("actual_working_hours"))
                        .annualLeaveUsed(rs.getInt("annual_leave_used"))
                        .lateEarlyHours(rs.getBigDecimal("late_early_hours"))
                        .overtimeHours(rs.getBigDecimal("overtime_hours"))
                        .standardWorkingHours(rs.getBigDecimal("standard_working_hours"))
                        .totalWorkingHours(rs.getBigDecimal("total_working_hours"))
                        .monthlyLateEarlyHours(rs.getBigDecimal("monthly_late_early_hours"))
                        .monthlyOvertimeHours(rs.getBigDecimal("monthly_overtime_hours"))
                        .department(DepartmentDto.builder()
                                .id(rs.getInt("d_department_id"))
                                .name(rs.getString("department_name"))
                                .build())
                        .employee(EmployeeDto.builder()
                                .id(rs.getInt("d_employee_id"))
                                .name(rs.getString("employee_name"))
                                .code(rs.getString("employee_code"))
                                .employeeType(rs.getString("employee_type"))
                                .build())
                        .org(OrgDto.builder()
                                .id(rs.getInt("d_org_id"))
                                .name(rs.getString("org_name"))
                                .build())
                        .employeeGrade(EmployeeGradeDto.builder()
                                .id(rs.getInt("d_employee_grade_id"))
                                .name(rs.getString("grade_name"))
                                .build())
                        .build();
                data.add(hTimeSheetSummaryDto);
            }

            Pagination pagination = queryEngine.getPagination("pos.d_h_timesheet_summary_v", parameter, req);
            log.info("Load pagination...");
            return GlobalReponsePagination.builder()
                    .data(data)
                    .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                    .status(org.springframework.http.HttpStatus.OK.value())
                    .pageSize(pagination.getPageSize())
                    .currentPage(pagination.getPage())
                    .totalPages(pagination.getTotalPage())
                    .totalItems(pagination.getTotalCount())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GlobalReponse findById(Integer integer) {

        return null;
    }

    @Override
    public GlobalReponse save(TimeSheetSummaryDto entity) {
        log.info("Save TimeSheet Request");

        TimeSheetSummary timeSheet = null;
        if(entity.getId() == null){
            timeSheet =  modelMapper.map(entity, TimeSheetSummary.class);
            timeSheet.setOrgId(0);
            timeSheet.setTenantId(AuditContext.getAuditInfo().getTenantId());
        }else{
            timeSheet = timeSheetRepository.findById(entity.getId())
                    .orElseThrow(() -> new PosException(messageSource.getMessage("timesheet_summary_not_exist", null, LocaleContextHolder.getLocale())));

            modelMapper.map(entity, timeSheet);
        }

        timeSheet = timeSheetRepository.save(timeSheet);
        return GlobalReponse.builder()
                .data(modelMapper.map(timeSheet, TimeSheetSummaryDto.class))
                .status(HttpStatus.SC_CREATED)
                .build();
    }

    @Override
    public GlobalReponse deleteById(Integer integer) {
        return null;
    }

    @Override
    public GlobalReponse getTimeSheetDetail(TimeSheetSummaryRequest req) {
        Parameter parameter = new Parameter();
        if(req.getMonth() != null){
            String monthStr = req.getMonth(); // "2025-08"
            YearMonth yearMonth = YearMonth.parse(monthStr);

            String start = yearMonth.atDay(1).toString(); // "2025-08-01"
            String end = yearMonth.atEndOfMonth().toString() + " 23:59:59"; // "2025-08-31 23:59:59"
            parameter.add("created", Param.getBetweenParam(start, end), Param.Logical.BETWEEN, Param.Relational.AND, Param.NONE);
        }

        parameter.add("d_employee_id", req.getEmployeeId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_h_timesheet_summary_v", parameter);

        DTimeSheetSummaryDto data = null;
        try {
            if (rs.next()) {
                Integer timeSheetSummaryId = rs.getInt("d_timesheet_summary_id");
                data = DTimeSheetSummaryDto.builder()
                        .actualWorkingHours(rs.getBigDecimal("actual_working_hours"))
                        .annualLeaveUsed(rs.getInt("annual_leave_used"))
                        .lateEarlyHours(rs.getBigDecimal("late_early_hours"))
                        .overtimeHours(rs.getBigDecimal("overtime_hours"))
                        .standardWorkingHours(rs.getBigDecimal("standard_working_hours"))
                        .totalWorkingHours(rs.getBigDecimal("total_working_hours"))
                        .monthlyLateEarlyHours(rs.getBigDecimal("monthly_late_early_hours"))
                        .monthlyOvertimeHours(rs.getBigDecimal("monthly_overtime_hours"))
                        .employee(EmployeeDto.builder()
                                .id(rs.getInt("d_employee_id"))
                                .name(rs.getString("employee_name"))
                                .code(rs.getString("employee_code"))
                                .employeeType(rs.getString("employee_type"))
                                .build())
                        .leaveApplication(getLeaveApplications(timeSheetSummaryId))
                        .overTimeLogDto(getOverTimeLogs(timeSheetSummaryId))
                        //.department(DepartmentDto.builder()
//                                .id(rs.getInt("d_department_id"))
//                                .name(rs.getString("department_name"))
//                                .build())
//                        .org(OrgDto.builder()
//                                .id(rs.getInt("d_org_id"))
//                                .name(rs.getString("org_name"))
//                                .build())
//                        .employeeGrade(EmployeeGradeDto.builder()
//                                .id(rs.getInt("d_employee_grade_id"))
//                                .name(rs.getString("grade_name"))
//                                .build())
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return GlobalReponse.builder()
                .data(data)
                .message(messageSource.getMessage("successfully", null, LocaleContextHolder.getLocale()))
                .status(org.springframework.http.HttpStatus.OK.value())
                .build();
    }

    private List<LeaveApplicationDto> getLeaveApplications(Integer timeSheetId){
        List<LeaveApplication> data = leaveApplicationRepository.findByTimesheetSummaryId(timeSheetId);

        if(data.isEmpty()){
            return null;
        }

        return data.stream().map(item -> modelMapper.map(item, LeaveApplicationDto.class)).collect(Collectors.toList());
    }

    private List<OverTimeLogDto> getOverTimeLogs(Integer timeSheetId){
        List<OverTimeLog> data = overTimeLogRepository.findByTimesheetSummaryId(timeSheetId);

        if(data.isEmpty()){
            return null;
        }

        return data.stream().map(item -> modelMapper.map(item, OverTimeLogDto.class)).collect(Collectors.toList());
    }
}
