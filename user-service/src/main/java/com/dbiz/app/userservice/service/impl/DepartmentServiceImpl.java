package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.Department;
import com.dbiz.app.userservice.repository.DepartmentRepository;
import com.dbiz.app.userservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.DepartmentQueryRequest;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final MessageSource messageSource;

    private final ModelMapper modelMapper;

    private final DepartmentRepository departmentRepository;

    private final QueryEngine queryEngine;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(DepartmentQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getKeyword() != null && !request.getKeyword().isBlank())
        {
            parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
            parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        }
        ResultSet rs = queryEngine.getRecords("d_department_get_v",
                parameter, request);
        List<DepartmentDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                DepartmentDto departmentDto = DepartmentDto.builder()
                        .departmentHeadName(rs.getString("department_head_name"))
                        .departmentHeadId(rs.getInt("d_department_id"))
                        .totalEmployees(rs.getInt("total_employees"))
                        .id(rs.getInt("d_department_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .isActive(rs.getString("is_active"))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .establishedDate(DateHelper.fromLocalDate(rs.getObject("established_date", LocalDate.class)))
                        .description(rs.getString("description") != null ? rs.getString("description") : "")
                        .build();
                listResponse.add(departmentDto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching departments", e);
        }
        Pagination pagination = queryEngine.getPagination("d_department_get_v", parameter, request);
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
        log.info("Finding department by id: {}", integer);
        Department department = this.departmentRepository.findById(integer)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage("department.not.found", null, LocaleContextHolder.getLocale())));
        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        return GlobalReponse.builder()
                .message("success")
                .data(departmentDto)
                .status(HttpStatus.OK.value()).build();
    }

    /**
     *
     * @param entity
     * @return
     */
    @Override
    public GlobalReponse save(DepartmentDto dto) {
        log.info("Saving department: {}", dto.getName());
        GlobalReponse response = new GlobalReponse();

        if (dto.getId() != null) {
            updateDepartment(dto, response);
        } else {
            createDepartment(dto, response);
        }

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

    @Override
    public Integer countEmployyesByDepartmentId(Integer departmentId) {
        return null;
    }


    private void updateDepartment(DepartmentDto dto, GlobalReponse response) {
        Department existing = departmentRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        messageSource.getMessage("department.not.found", null, LocaleContextHolder.getLocale())));

        modelMapper.map(dto, existing);

        if (dto.getEstablishedDate() != null) {
            existing.setEstablishedDate(DateHelper.toLocalDate(dto.getEstablishedDate()));
        }

        Department saved = departmentRepository.save(existing);

        DepartmentDto updatedDto = modelMapper.map(saved, DepartmentDto.class);
        updatedDto.setEstablishedDate(DateHelper.fromLocalDate(saved.getEstablishedDate()));

        response.setData(updatedDto);
        response.setMessage(messageSource.getMessage("department.update.success", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.OK.value());
    }

    private void createDepartment(DepartmentDto dto, GlobalReponse response) {
        validateParam(dto);

        Department entity = modelMapper.map(dto, Department.class);

        String initials = generateInitials(dto.getName());
        String newCode = initials + departmentRepository.getMaxDepartmentID();
        entity.setCode(newCode);

        entity.setEstablishedDate(DateHelper.toLocalDate(dto.getEstablishedDate()));
        entity.setOrgId(0); // hoặc lấy từ context nếu có
        entity.setTenantId(0); // hoặc từ AuditContext nếu đa tenant

        Department saved = departmentRepository.save(entity);

        DepartmentDto createdDto = modelMapper.map(saved, DepartmentDto.class);
        createdDto.setEstablishedDate(DateHelper.fromLocalDate(saved.getEstablishedDate()));

        response.setData(createdDto);
        response.setMessage(messageSource.getMessage("department.created.success", null, LocaleContextHolder.getLocale()));
        response.setStatus(HttpStatus.CREATED.value());
    }

    private String generateInitials(String name) {
        if (name == null || name.isBlank()) return "DP"; // fallback
        StringBuilder initials = new StringBuilder();
        for (String word : name.trim().split("\\s+")) {
            if (!word.isEmpty()) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return initials.toString();
    }
    private Boolean validateParam(DepartmentDto param)
    {
        if(param.getName() == null || param.getName().isEmpty()) {
            log.error("Department name is required");
            throw new IllegalArgumentException(messageSource.getMessage("department.name.required", null, LocaleContextHolder.getLocale()));
        }
        if(param.getEstablishedDate() == null || param.getEstablishedDate().isEmpty()) {
            log.error("Department established date is required");
            throw new IllegalArgumentException(messageSource.getMessage("department.established.date.required", null, LocaleContextHolder.getLocale()));
        }
        if(param.getDepartmentHeadId() == null) {
            log.error("Department head is required and must be greater than zero");
            throw new IllegalArgumentException(messageSource.getMessage("department.head.required", null, LocaleContextHolder.getLocale()));
        }
        return true;
    }
    /**
     *
     * @param departmentId
     * @return
     */

}
