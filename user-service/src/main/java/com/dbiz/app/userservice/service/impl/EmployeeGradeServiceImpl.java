package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.Employee;
import com.dbiz.app.userservice.domain.EmployeeGrade;
import com.dbiz.app.userservice.repository.EmployeeGradeRepository;
import com.dbiz.app.userservice.service.EmployeeGradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.dto.userDto.EmployeeGradeDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.EmployeeGradeQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeGradeServiceImpl implements EmployeeGradeService {
    private final MessageSource messageSource;

    private final EmployeeGradeRepository employeeGradeRepository;

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(EmployeeGradeQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getKeyword() != null && !request.getKeyword().isBlank())
        {
            parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
            parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        }
        ResultSet rs = queryEngine.getRecords("d_employee_grade_get_v",
                parameter, request);
        List<EmployeeGradeDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                EmployeeGradeDto employeeGradeDto = EmployeeGradeDto.builder()
                        .id(rs.getInt("d_employee_grade_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .tenantId(rs.getInt("d_tenant_id"))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .jobDescription(rs.getString("job_description"))
                        .permission(rs.getString("permission"))
                        .level(rs.getBigDecimal("level"))
                        .experienceRequired(rs.getString("experience_required"))
                        .baseSalaryMin(rs.getBigDecimal("base_salary_min"))
                        .experienceRequiredName(rs.getString("experience_required_name") != null ? rs.getString("experience_required_name") : "")
                        .permissionName(rs.getString("permission_name") != null ? rs.getString("permission_name") : "")
                          .build();
                listResponse.add(employeeGradeDto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching departments", e);
        }
        Pagination pagination = queryEngine.getPagination("d_employee_grade_get_v", parameter, request);
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
    public GlobalReponse save(EmployeeGradeDto dto) {
        log.info("Saving EmployeeGrade: {}", dto);

        EmployeeGrade employeeGrade = modelMapper.map(dto, EmployeeGrade.class);
        GlobalReponse response = new GlobalReponse();

        if (employeeGrade.getId() != null) {
            updateExistingEmployeeGrade(employeeGrade, response);
        } else {
            createNewEmployeeGrade(employeeGrade, response);
        }

        response.setData(modelMapper.map(employeeGrade, EmployeeGradeDto.class));
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

    private void updateExistingEmployeeGrade(EmployeeGrade updatedData, GlobalReponse response) {
        EmployeeGrade existingEntity = employeeGradeRepository.findById(updatedData.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeGrade not found with id: " + updatedData.getId()));

        modelMapper.map(updatedData, existingEntity);
        employeeGradeRepository.save(existingEntity);

        response.setMessage(messageSource.getMessage("employeeGrade.update.success", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
    }

    private void createNewEmployeeGrade(EmployeeGrade entity, GlobalReponse response) {
        String initials = generateInitialsFromName(entity.getName());
        entity.setCode(initials + employeeGradeRepository.getMaxEmployeeGradeId());
        entity.setOrgId(0);
        entity.setTenantId(0);

        employeeGradeRepository.saveAndFlush(entity);

        response.setMessage(messageSource.getMessage("employeeGrade.created.success", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.CREATED.value());
    }

    private String generateInitialsFromName(String name) {
        if (name == null || name.isBlank()) return "GRD"; // fallback
        StringBuilder initials = new StringBuilder();
        for (String word : name.trim().split("\\s+")) {
            if (!word.isEmpty()) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return initials.toString();
    }

}
