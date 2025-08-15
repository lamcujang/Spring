package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.ConfigShift;
import com.dbiz.app.userservice.domain.ConfigShiftEmployee;
import com.dbiz.app.userservice.repository.ConfigShiftEmployeeRepository;
import com.dbiz.app.userservice.repository.ConfigShiftRepository;
import com.dbiz.app.userservice.service.ConfigShiftEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.ConfigShiftEmployeeDto;
import org.common.dbiz.dto.userDto.nested.DepartmentDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigShiftEmployeeQueryRequest;
import org.common.dbiz.request.userRequest.ConfigShiftQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftEmployeeServiceImpl  implements ConfigShiftEmployeeService {
    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final EntityManager entityManager;

    private final ConfigShiftEmployeeRepository configShiftEmployeeRepository;

    private final MessageSource messageSource;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ConfigShiftEmployeeQueryRequest request) {
        Parameter parameter = new Parameter();
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            parameter.add("employee_name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
            parameter.add("employee_code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        }
        if(request.getDepartmentId() != null)
        {
            parameter.add("d_department_id", request.getDepartmentId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        }
        if(request.getEmployeeGradeId() != null)
        {
            parameter.add("d_employee_grade_id", request.getEmployeeGradeId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        }
        if(request.getOrgId()!=null)
        {
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
        }
        if(request.getWorkStatus()!= null && !request.getWorkStatus().isBlank())
        {
            parameter.add("work_status", request.getWorkStatus(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        }
        if(request.getConfigShiftId() != null)
        {
            parameter.add("d_config_shift_id", request.getConfigShiftId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
//            parameter.add("d_config_shift_id", null, Param.Logical.IS_NULL, Param.Relational.OR, Param.NONE);
        }
//        if(request.getConfigShiftId() != null)
//            parameter.add("d_config_shift_id", null , Param.Logical.IS_NULL, Param.Relational.AND, Param.NONE);

            ResultSet rs = queryEngine.getRecords("d_config_shift_employee_get_v",
                parameter, request);
        List<ConfigShiftEmployeeDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                ConfigShiftEmployeeDto dto = ConfigShiftEmployeeDto.builder()
                        .id(rs.getInt("d_config_shift_employee_id")  == 0 ? null : rs.getInt("d_config_shift_employee_id"))
//                        .orgId(rs.getInt("d_org_id"))
//                        .orgName(rs.getString("org_name"))
                        .configShiftId(rs.getInt("d_config_shift_id") == 0 ? null : rs.getInt("d_config_shift_id"))
//                        .employeeId(rs.getInt("d_employee_id"))
                        .isActive(rs.getString("is_active"))
//                        .employeeName( rs.getString("employee_name"))
//                        .departmentName(rs.getString("department_name"))
                        .workStatusName(rs.getString("work_status_name"))
                        .build();
                dto.setOrgDto(OrgDto.builder().id(rs.getInt("d_org_id"))
                        .name(rs.getString("org_name")).build());
                dto.setEmployeeDto(EmployeeDto.builder()
                        .id(rs.getInt("d_employee_id"))
                        .name(rs.getString("employee_name"))
                        .build());
                dto.setDepartmentDto(DepartmentDto.builder()
                        .id(rs.getInt("d_department_id"))
                        .name(rs.getString("department_name"))
                        .build());
                listResponse.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching departments", e);
        }
        Pagination pagination = queryEngine.getPagination("d_config_shift_employee_get_v", parameter, request);
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
    public GlobalReponse save(ConfigShiftEmployeeDto entity) {
        this.configShiftEmployeeRepository.updateIsActiveByShiftId("N",entity.getConfigShiftId());
        ConfigShiftEmployee checkExit = this.configShiftEmployeeRepository.findByEmployeeIdAndConfigShiftId(entity.getEmployeeDto().getId(), entity.getConfigShiftId());
        if(checkExit == null )
        {
            checkExit = modelMapper.map(entity, ConfigShiftEmployee.class);
            castField(entity, checkExit);
            checkExit.setIsActive("Y");
            checkExit = this.configShiftEmployeeRepository.save(checkExit);
        }else {
            checkExit.setConfigShiftId(entity.getConfigShiftId());
            castField(entity, checkExit);
            checkExit.setIsActive("Y");
            checkExit = this.configShiftEmployeeRepository.save(checkExit);
        }

        return null;
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

    /**
     *
     * @param params
     * @return
     */
    @Override
    public List<ConfigShiftEmployeeDto> saveAll(List<ConfigShiftEmployeeDto> params, ConfigShift configShift) {
        if(params == null || params.isEmpty())
            return new ArrayList<>();
        this.configShiftEmployeeRepository.updateIsActiveByShiftId("N",params.get(0).getConfigShiftId());
        List<ConfigShiftEmployeeDto> listResponse = new ArrayList<>();
       params.forEach(item->{
           ConfigShiftEmployee checkExit = this.configShiftEmployeeRepository.findByEmployeeIdAndConfigShiftId(item.getEmployeeDto().getId(), item.getConfigShiftId());
           if(checkExit == null )
           {
               checkExit = modelMapper.map(item, ConfigShiftEmployee.class);
               checkExit.setConfigShiftId(configShift.getId());
               checkExit.setTenantId(0);
               checkExit.setIsActive("Y");
                castField(item, checkExit);
                checkExit.setOrgId(configShift.getOrgId() != null ? configShift.getOrgId() : null);
               checkExit = this.configShiftEmployeeRepository.save(checkExit);
           }else {
               checkExit.setConfigShiftId(configShift.getId());
               checkExit.setIsActive("Y");
                castField(item, checkExit);
               checkExit.setOrgId(configShift.getOrgId() != null ? configShift.getOrgId() : null);
               checkExit = this.configShiftEmployeeRepository.save(checkExit);
           }
           listResponse.add(modelMapper.map(checkExit, ConfigShiftEmployeeDto.class));
       });


        return listResponse;
    }

    private void castField(ConfigShiftEmployeeDto dto , ConfigShiftEmployee employee)
    {
        if(dto.getEmployeeDto() != null)
            employee.setEmployeeId(dto.getEmployeeDto().getId() != null ? dto.getEmployeeDto().getId() : null);
        if(dto.getOrgDto() != null)
            employee.setOrgId(dto.getOrgDto().getId() != null ? dto.getOrgDto().getId() : null);
    }
}
