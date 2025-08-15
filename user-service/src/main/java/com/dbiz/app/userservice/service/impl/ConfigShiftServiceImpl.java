package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.ConfigShift;
import com.dbiz.app.userservice.repository.ConfigShiftRepository;
import com.dbiz.app.userservice.repository.ConfigTimeKeepingRepository;
import com.dbiz.app.userservice.service.ConfigShiftEmployeeService;
import com.dbiz.app.userservice.service.ConfigShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.userDto.ConfigShiftDto;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.dto.userDto.DepartmentDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.EmployeeGradeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
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
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigShiftServiceImpl implements ConfigShiftService {
    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final EntityManager entityManager;

    private final ConfigShiftRepository configShiftRepository;

    private final MessageSource messageSource;

    private final ConfigShiftEmployeeService configShiftEmployeeService;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ConfigShiftQueryRequest request) {
        Parameter parameter = new Parameter();
        if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
            parameter.add("name", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
            parameter.add("code", request.getKeyword(), Param.Logical.LIKE, Param.Relational.OR, Param.NONE);
        }
        if (request.getOrgId() != null) {
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
        }
        if (request.getShiftType() != null) {
            parameter.add("shift_type", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);

        }
        if (request.getEmployeeCreatedBy() != null) {
            parameter.add("employee_created_by", request.getEmployeeCreatedBy(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
        }
        if (request.getStartDate() != null && request.getEndDate() != null) {
            parameter.add("valid_from", DateHelper.toLocalDate(request.getStartDate()), Param.Logical.GREATER_THAN_EQ, Param.Relational.AND, Param.NONE);
            parameter.add("valid_to", DateHelper.toLocalDate(request.getEndDate()), Param.Logical.LESS_THAN_EQ, Param.Relational.AND, Param.NONE);
        }
        ResultSet rs = queryEngine.getRecords("d_config_shift_get_v",
                parameter, request);
        List<ConfigShiftDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                String workingDaysRaw = rs.getString("working_days");
                String[] workingDaysArray = workingDaysRaw != null ? workingDaysRaw.split(",") : new String[0];
                ConfigShiftDto dto = ConfigShiftDto.builder()
                        .id(rs.getInt("d_config_shift_id"))
//                        .orgId(rs.getInt("d_org_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("name"))
                        .shiftType(rs.getString("shift_type"))
                        .startTime(rs.getString("start_time"))
                        .breakDurationMinutes(rs.getInt("break_duration_minutes"))
                        .endTime(rs.getString("end_time"))
                        .checkinTime(rs.getString("checkin_time"))
                        .checkoutTime(rs.getString("checkout_time"))
                        .validFrom(rs.getString("valid_from"))
                        .validTo(rs.getString("valid_to"))
                        .workingDays(workingDaysArray) // Nếu DB dùng array
                        .isActive(rs.getString("is_active"))
                        .isValidTo(rs.getString("is_valid_to"))
//                        .employeeCreatedBy(rs.getInt("d_employee_created_by"))
//                        .orgName(rs.getString("org_name"))
//                        .employeeCreatedByName(rs.getString("employee_created_name"))
                        .shiftTypeName(rs.getString("shift_type_name"))
                        .build();
                EmployeeDto employeeCreatedByDto = EmployeeDto.builder()
                        .id(rs.getInt("d_employee_created_by"))
                        .name(rs.getString("employee_created_name"))
                        .build();
                dto.setEmployeeCreatedDto(employeeCreatedByDto);
                dto.setOrgDto(OrgDto.builder()
                        .id(rs.getInt("d_org_id"))
                        .name(rs.getString("org_name"))
                        .build());

                listResponse.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching departments", e);
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
    @Transactional
    public GlobalReponse save(ConfigShiftDto dto) {
        ConfigShift savedEntity;
        boolean isCreate = dto.getId() == null;

        if (isCreate) {
            ConfigShift newEntity = modelMapper.map(dto, ConfigShift.class);
            newEntity.setTenantId(0);
            newEntity.setCode(generateInitials(newEntity.getName()));
            applyDtoFields(dto, newEntity);

            savedEntity = configShiftRepository.save(newEntity);
        } else {
            ConfigShift existingEntity = configShiftRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("ConfigShift not found"));
            modelMapper.map(dto, existingEntity);
            applyDtoFields(dto, existingEntity);
            savedEntity = configShiftRepository.saveAndFlush(existingEntity);
        }

        // Save configShiftEmployees if any
        if (dto.getConfigShiftEmployees() != null && !dto.getConfigShiftEmployees().isEmpty()) {

            dto.setConfigShiftEmployees(
                    configShiftEmployeeService.saveAll(dto.getConfigShiftEmployees(), savedEntity));
        }

        // Prepare response DTO
        ConfigShiftDto responseDto = modelMapper.map(savedEntity, ConfigShiftDto.class);
        if (dto.getOrgDto() != null) {
            responseDto.setOrgDto(dto.getOrgDto());
        }
        if (dto.getEmployeeCreatedDto() != null) {
            responseDto.setEmployeeCreatedDto(dto.getEmployeeCreatedDto());
        }

        return GlobalReponse.builder()
                .data(responseDto)
                .message("ConfigShift saved successfully")
                .status(200)
                .errors("")
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


    private void partDateConfigShift(ConfigShift configShift, ConfigShiftDto configShiftDto)
    {
        if (configShiftDto.getValidFrom() != null) {
            configShift.setValidFrom(DateHelper.toLocalDate(configShiftDto.getValidFrom()));
        }
        if (configShiftDto.getValidTo() != null) {
            configShift.setValidTo(DateHelper.toLocalDate(configShiftDto.getValidTo()));
        }
        if (configShiftDto.getStartTime() != null) {
            configShift.setStartTime(DateHelper.parseStringToLocalTime(configShiftDto.getStartTime()));
        }
        if (configShiftDto.getEndTime() != null) {
            configShift.setEndTime(DateHelper.parseStringToLocalTime(configShiftDto.getEndTime()));
        }
        if (configShiftDto.getCheckinTime() != null) {
            configShift.setCheckinTime(DateHelper.parseStringToLocalTime(configShiftDto.getCheckinTime()));
        }
        if (configShiftDto.getCheckoutTime() != null) {
            configShift.setCheckoutTime(DateHelper.parseStringToLocalTime(configShiftDto.getCheckoutTime()));
        }
    }


    private String generateInitials(String name) {
        if (name == null || name.isBlank()) return "CH"; // fallback
        StringBuilder initials = new StringBuilder();
        for (String word : name.trim().split("\\s+")) {
            if (!word.isEmpty()) {
                initials.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return initials.toString();
    }

    private void applyDtoFields(ConfigShiftDto dto, ConfigShift entity) {
        // Working days
        if (dto.getWorkingDays() != null && dto.getWorkingDays().length > 0) {
            entity.setWorkingDays(String.join(",", dto.getWorkingDays()));
        }

        // partDateConfigShift xử lý logic riêng
        this.partDateConfigShift(entity, dto);

        // Org
        if (dto.getOrgDto() != null && dto.getOrgDto().getId() != null) {
            entity.setOrgId(dto.getOrgDto().getId());
        }

        // Created by
        if (dto.getEmployeeCreatedDto() != null && dto.getEmployeeCreatedDto().getId() != null) {
            entity.setEmployeeCreatedBy(dto.getEmployeeCreatedDto().getId());
        }
    }

}
