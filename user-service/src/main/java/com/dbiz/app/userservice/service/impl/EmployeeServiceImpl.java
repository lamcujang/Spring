package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.Employee;
import com.dbiz.app.userservice.domain.EmployeeContact;
import com.dbiz.app.userservice.repository.EmployeeRepository;
import com.dbiz.app.userservice.service.DepartmentService;
import com.dbiz.app.userservice.service.EmployeeBankService;
import com.dbiz.app.userservice.service.EmployeeContactService;
import com.dbiz.app.userservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.*;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.helper.DateUtils;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeQueryRequest;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final EmployeeBankService employeeBankService;

    private final EmployeeContactService employeeContactService;

    private final DepartmentService departmentService;

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final MessageSource messageSource;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(EmployeeQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getKeyword() != null && !request.getKeyword().isBlank()) {
            parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
            parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        }
        if(request.getEmployeeGradeId()!= null )
            parameter.add("d_employee_grade_id", request.getEmployeeGradeId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        if(request.getDepartmentId() != null)
            parameter.add("d_department_id", request.getDepartmentId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        if(request.getWorkStatus() != null && !request.getWorkStatus().isBlank())
            parameter.add("work_status", request.getWorkStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        if(request.getOrgId() != null)
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);

        ResultSet rs = queryEngine.getRecords("d_employee_get_v",
                parameter, request);
        List<EmployeeDto> listResponse = new ArrayList<>();
        try{
            while(rs.next())
            {

                EmployeeDto item = EmployeeDto.builder()
                        // Thông tin chung
                        .id(rs.getInt("d_employee_id"))
//                        .orgId(rs.getInt("d_org_id"))
                        .isActive(rs.getString("is_active"))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .gender(rs.getString("gender"))
                        // Ngày tháng
                        .birthday(rs.getString("birthday"))
                        .placeOfBirth(rs.getString("place_of_birth"))
                        .ethnicity(rs.getString("ethnicity"))
                        .nationality(rs.getString("nationality"))
                        .identificationNumber(rs.getString("identification_number"))
                        .dateOfIssue(rs.getString("date_of_issue"))
                        .placeOfIssue(rs.getString("place_of_issue"))
                        .permanentAddress(rs.getString("permanent_address"))
                        // Loại nhân viên, trạng thái, ngày gia nhập
                        .employeeType(rs.getString("employee_type"))
                        .joinDate(rs.getString("join_date"))
                        .workStatus(rs.getString("work_status"))
                        // Thông tin liên lạc
                        .phone(rs.getString("phone"))
                        .personalEmail(rs.getString("personal_email"))
                        .corporateEmail(rs.getString("corporate_email"))
                        .contactAddress(rs.getString("contact_address"))
                        // Hợp đồng
                        .contractNumber(rs.getString("contract_number"))
                        .contractType(rs.getString("contract_type"))
                        .contractStartDate(rs.getString("contract_start_date"))
                        .contractEndDate(rs.getString("contract_end_date"))
                        .contractSignedDate(rs.getString("contract_signed_date"))
                        .contractTerminationDate(rs.getString("contract_termination_date"))
                        // Lương
                        .baseSalary(rs.getBigDecimal("base_salary"))
                        .probationSalary(rs.getBigDecimal("probation_salary"))
                        .insuranceSalary(rs.getBigDecimal("insurance_salary"))
                        // Hộ chiếu/XH
                        .originalContractNumber(rs.getString("original_contract_number"))
                        .hasSocialInsurance(rs.getString("has_social_insurance"))
                        .socialInsuranceNumber(rs.getString("social_insurance_number"))
                        .hasHealthInsurance(rs.getString("has_health_insurance"))
                        .healthInsuranceNumber(rs.getString("health_insurance_number"))
                        .hasAccidentInsurance(rs.getString("has_accident_insurance"))
                        .accidentInsuranceNumber(rs.getString("accident_insurance_number"))
                        // Phụ cấp
                        .mealAllowance(rs.getBigDecimal("meal_allowance"))
                        .transportAllowance(rs.getBigDecimal("transport_allowance"))
                        .clothingAllowance(rs.getBigDecimal("clothing_allowance"))
                        // Thưởng
                        .attendanceBonus(rs.getBigDecimal("attendance_bonus"))
                        .referralBonus(rs.getBigDecimal("referral_bonus"))
                        .holidayBonus(rs.getBigDecimal("holiday_bonus"))
                        .phoneAllowance(rs.getBigDecimal("phone_allowance"))
                        .nightShiftAllowance(rs.getBigDecimal("night_shift_allowance"))
                        .imageId(rs.getInt("image_id"))
//                        .departmentId(rs.getInt("d_department_id"))
//                        .employeeGradeId(rs.getInt("d_employee_grade_id"))
//                        .departmentName(rs.getString("department_name"))
//                        .employeeGradeName(rs.getString("employee_grade_name"))
//                        .orgName(rs.getString("org_name"))
                        .workStatusName(rs.getString("work_status_name"))
                        .employeeTypeName(rs.getString("employee_type_name"))
                        .contractTypeName(rs.getString("contract_type_name"))
                        .build();
                org.common.dbiz.dto.userDto.nested.DepartmentDto departmentDto = org.common.dbiz.dto.userDto.nested.DepartmentDto.builder()
                        .id(rs.getInt("d_department_id"))
                        .name(rs.getString("department_name")).build();
                item.setDepartmentDto(departmentDto);
                org.common.dbiz.dto.userDto.nested.OrgDto orgDto = org.common.dbiz.dto.userDto.nested.OrgDto.builder()
                        .id(rs.getInt("d_org_id"))
                        .name(rs.getString("org_name"))
                        .build();
                item.setOrgDto(orgDto);
                org.common.dbiz.dto.userDto.nested.EmployeeGradeDto employeeGradeDto = org.common.dbiz.dto.userDto.nested.EmployeeGradeDto.builder()
                        .id(rs.getInt("d_employee_grade_id"))
                        .name(rs.getString("employee_grade_name"))
                        .build();
                item.setEmployeeGradeDto(employeeGradeDto);
                listResponse.add(item);
            }
        }catch (Exception e)
        {
            log.error("Error while fetching employees: {}", e.getMessage());
            throw new RuntimeException("Error while fetching employees", e);
        }
        Pagination pagination = queryEngine.getPagination("d_config_shift_get_v", parameter, request);
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
    public GlobalReponse save(EmployeeDto dto) {
        log.info("Saving employee: {}", dto);

        validateDepartmentCapacity(dto.getDepartmentDto().getId());

        Employee employee = modelMapper.map(dto, Employee.class);
        Employee savedEmployee;
        boolean isUpdate = employee.getId() != null;

        if (isUpdate) {
            savedEmployee = updateEmployee(dto);
        } else {
            savedEmployee = createEmployee(dto);
        }

        handleEmployeeDetails(dto, savedEmployee.getId());

        EmployeeDto resultDto = modelMapper.map(savedEmployee, EmployeeDto.class);
        if(dto.getOrgDto()  != null)
            resultDto.setOrgDto(modelMapper.map(dto.getOrgDto(), org.common.dbiz.dto.userDto.nested.OrgDto.class));
        if(dto.getDepartmentDto() != null)
            resultDto.setDepartmentDto(modelMapper.map(dto.getDepartmentDto(), org.common.dbiz.dto.userDto.nested.DepartmentDto.class));
        if(dto.getEmployeeGradeDto() != null)
            resultDto.setEmployeeGradeDto(modelMapper.map(dto.getEmployeeGradeDto(), org.common.dbiz.dto.userDto.nested.EmployeeGradeDto.class));
        return GlobalReponse.builder()
                .data(resultDto)
                .status(isUpdate ? HttpStatus.OK.value() : HttpStatus.CREATED.value())
                .message(isUpdate ? "Employee updated successfully" : "Employee created successfully")
                .build();
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

    // convert String -> LocalDate
    private void converDate(EmployeeDto employeeDto, Employee employee) {
        DateUtils.safeSetDate(employeeDto.getJoinDate(), employee::setJoinDate);
        DateUtils.safeSetDate(employeeDto.getContractStartDate(), employee::setContractStartDate);
        DateUtils.safeSetDate(employeeDto.getContractEndDate(), employee::setContractEndDate);
        DateUtils.safeSetDate(employeeDto.getDateOfIssue(), employee::setDateOfIssue);
        DateUtils.safeSetDate(employeeDto.getBirthday(), employee::setBirthday);
        DateUtils.safeSetDate(employeeDto.getContractSignedDate(), employee::setContractSignedDate);
        DateUtils.safeSetDate(employeeDto.getContractTerminationDate(), employee::setContractTerminationDate);
    }

    private void validateDepartmentCapacity(Integer departmentId) {
        DepartmentDto department = modelMapper.map(departmentService.findById(departmentId).getData(), DepartmentDto.class);
        Integer currentCount = employeeRepository.countByDepartmentId(department.getId());
        if (currentCount >= department.getTotalEmployees()) {
            throw new RuntimeException("Department has reached maximum number of employees");
        }
    }

    private Employee updateEmployee(EmployeeDto dto) {
        Employee existing = employeeRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        modelMapper.map(dto, existing);
        converDate(dto, existing);
        return employeeRepository.save(existing);
    }

    private Employee createEmployee(EmployeeDto dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        employee.setCode("NV" + employeeRepository.getMaxEmployeeId());
        employee.setTenantId(0);
        converDate(dto, employee);
        return employeeRepository.saveAndFlush(employee);
    }

    private EmployeeDto handleEmployeeDetails(EmployeeDto dto, Integer employeeId) {
        List<EmployeeBankDto> listEmployeeBankDto = new ArrayList<>();
        List<EmployeeContactDto> listEmployeeContactDto = new ArrayList<>();
        if (dto.getEmployeeBankList() != null && !dto.getEmployeeBankList().isEmpty()) {
            dto.getEmployeeBankList().forEach(bank -> {
                bank.setEmployeeId(employeeId);
                bank.setOrgId(dto.getOrgDto().getId());
                GlobalReponse response =  employeeBankService.save(bank);
                EmployeeBankDto employeeBankDto = modelMapper.map(response.getData(), EmployeeBankDto.class);
                listEmployeeBankDto.add(employeeBankDto);
            });
        }

        if (dto.getEmployeeContactList() != null && !dto.getEmployeeContactList().isEmpty()) {
            dto.getEmployeeContactList().forEach(contact -> {
                contact.setTenantId(0);
                contact.setOrgId(0);
                contact.setEmployeeId(employeeId);
                GlobalReponse response = employeeContactService.save(contact);
                EmployeeContactDto employeeContactDto = modelMapper.map(response.getData(), EmployeeContactDto.class);
                listEmployeeContactDto.add(employeeContactDto);
            });
        }
        dto.setEmployeeBankList( listEmployeeBankDto );
        dto.setEmployeeContactList( listEmployeeContactDto );
        return dto;
    }

}
