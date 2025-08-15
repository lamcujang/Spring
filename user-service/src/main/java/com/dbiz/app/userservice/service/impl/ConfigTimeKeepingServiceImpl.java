package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.ConfigTimeKeeping;
import com.dbiz.app.userservice.repository.ConfigTimeKeepingRepository;
import com.dbiz.app.userservice.service.ConfigTimeKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.PromotionDto;
import org.common.dbiz.dto.userDto.ConfigTimeKeepingDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.dto.userDto.nested.OrgDto;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.ConfigTimeKeepingQueryRequest;
import org.common.dbiz.sql.Pagination;
import org.common.dbiz.sql.Param;
import org.common.dbiz.sql.Parameter;
import org.modelmapper.Conditions;
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
public class ConfigTimeKeepingServiceImpl implements ConfigTimeKeepingService {

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final EntityManager entityManager;

    private final ConfigTimeKeepingRepository configTimeKeepingRepository;

    private final MessageSource messageSource;

    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(ConfigTimeKeepingQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getOrgId()!= null )
        {
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.OR, Param.NONE);
        }
        ResultSet rs = queryEngine.getRecords("d_config_time_keeping_get_v",
                parameter, request);
        List<ConfigTimeKeepingDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                OrgDto orgDto = OrgDto.builder()
                        .id(rs.getInt("d_org_id"))
                        .code(rs.getString("org_code"))
                        .name(rs.getString("org_name"))
                        .build();
                EmployeeDto approverDto = EmployeeDto.builder()
                        .id(rs.getInt("approver_id"))
                        .code(rs.getString("approver_code"))
                        .name(rs.getString("approver_name"))
                        .build();
                EmployeeDto warningRecipientDto = EmployeeDto.builder()
                        .id(rs.getInt("warning_recipient_id"))
                        .code(rs.getString("warning_recipient_code"))
                        .name(rs.getString("warning_recipient_name"))
                        .build();
                ConfigTimeKeepingDto dto = ConfigTimeKeepingDto.builder()
                        .id(rs.getInt("d_config_time_keeping_id"))
                        .standardWorkHours(rs.getBigDecimal("standard_work_hours"))
                        .maxLunchBreakMinutes(rs.getBigDecimal("max_lunch_break_minutes"))
                        .maxLateMinutes(rs.getBigDecimal("max_late_minutes"))
                        .maxEarlyLeaveMinutes(rs.getBigDecimal("max_early_leave_minutes"))
                        .autoMarkAbsentIfNoCheckin(rs.getString("auto_mark_absent_if_no_checkin"))
                        .allowCheckoutAfterWorkHours(rs.getString("allow_checkout_after_work_hours"))
                        .requireReasonForLateCheckin(rs.getString("require_reason_for_late_checkin"))
                        .allowSupplementRequest(rs.getString("allow_supplement_request"))
                        .requestDeadlineDays(rs.getBigDecimal("request_deadline_days"))
                        .maxCheckinAttemptsPerDay(rs.getBigDecimal("max_checkin_attempts_per_day"))
                        .markUnpaidLeaveIfNoCheckinAndNoRequest(rs.getString("mark_unpaid_leave_if_no_checkin_and_no_request"))
                        .noFaultIfLeaveRequestExists(rs.getString("no_fault_if_leave_request_exists"))
                        .allowSingleCheckinForMultipleShifts(rs.getString("allow_single_checkin_for_multiple_shifts"))
                        .autoSumWorkingHoursBetweenShifts(rs.getString("auto_sum_working_hours_between_shifts"))
                        .mergeShiftsWithinMinutes(rs.getBigDecimal("merge_shifts_within_minutes"))
                        .maxShiftsPerDay(rs.getBigDecimal("max_shifts_per_day"))
                        .minRestBetweenShiftsMinutes(rs.getBigDecimal("min_rest_between_shifts_minutes"))
                        .notifyIfNoCheckin2Days(rs.getString("notify_if_no_checkin_2_days"))
                        .warnIfLate3TimesPerWeek(rs.getString("warn_if_late_3_times_per_week"))
                        .warnIfEarlyLeave3TimesPerWeek(rs.getString("warn_if_early_leave_3_times_per_week"))
                        .notifyAt(rs.getString("notify_at")) // Bạn có thể xử lý LocalTime nếu cần
                        .notifyByEmail(rs.getString("notify_by_email"))
                        .notifyBySms(rs.getString("notify_by_sms"))
                        .notifyInApp(rs.getString("notify_in_app"))

                        .build();
                dto.setOrgDto(orgDto);
                dto.setApproverDto(approverDto);
                dto.setWarningRecipientDto(warningRecipientDto);
                listResponse.add(dto);
            }

        } catch (Exception e) {
           throw new RuntimeException("Error while fetching ConfigTimeKeeping records: " + e.getMessage(), e);
        }
        Pagination pagination = queryEngine.getPagination("d_config_time_keeping_get_v", parameter, request);
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
    public GlobalReponse save(ConfigTimeKeepingDto dto) {
        log.info("Saving ConfigTimeKeeping: {}", dto);
        GlobalReponse.GlobalReponseBuilder responseBuilder = GlobalReponse.builder();
        ConfigTimeKeeping savedEntity;

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        if (dto.getId() != null) {
            // Update
            ConfigTimeKeeping existing = configTimeKeepingRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("ConfigTimeKeeping not found with id: " + dto.getId()));
            modelMapper.map(dto, existing);
            applySpecialFields(dto, existing);
            savedEntity = configTimeKeepingRepository.save(existing);
            responseBuilder.message("ConfigTimeKeeping updated successfully").status(200);
        } else {
            // Create
            ConfigTimeKeeping newEntity = modelMapper.map(dto, ConfigTimeKeeping.class);
            newEntity.setTenantId(0);
            applySpecialFields(dto, newEntity);
            savedEntity = configTimeKeepingRepository.save(newEntity);
            responseBuilder.message("ConfigTimeKeeping created successfully").status(201);
        }

        ConfigTimeKeepingDto responseDto = modelMapper.map(savedEntity, ConfigTimeKeepingDto.class);
        enrichResponseDto(responseDto, dto, savedEntity);

        return responseBuilder
                .data(responseDto)
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

    private void applySpecialFields(ConfigTimeKeepingDto dto, ConfigTimeKeeping entity) {
        if (dto.getNotifyAt() != null) {
            entity.setNotifyAt(DateHelper.parseStringToLocalTime(dto.getNotifyAt()));
        }

        if (dto.getOrgDto() != null && dto.getOrgDto().getId() != null) {
            entity.setOrgId(dto.getOrgDto().getId());
        }

        if (dto.getApproverDto() != null && dto.getApproverDto().getId() != null) {
            entity.setApproverId(dto.getApproverDto().getId());
        }

        if (dto.getWarningRecipientDto() != null && dto.getWarningRecipientDto().getId() != null) {
            entity.setWarningRecipientId(dto.getWarningRecipientDto().getId());
        }
    }

    private void enrichResponseDto(ConfigTimeKeepingDto responseDto, ConfigTimeKeepingDto inputDto, ConfigTimeKeeping savedEntity) {
        if (inputDto.getOrgDto() != null) {
            responseDto.setOrgDto(modelMapper.map(inputDto.getOrgDto(), OrgDto.class));
        }

        if (inputDto.getApproverDto() != null) {
            responseDto.setApproverDto(modelMapper.map(inputDto.getApproverDto(), EmployeeDto.class));
        }

        if (inputDto.getWarningRecipientDto() != null) {
            responseDto.setWarningRecipientDto(modelMapper.map(inputDto.getWarningRecipientDto(), EmployeeDto.class));
        }

        if (savedEntity.getNotifyAt() != null) {
            responseDto.setNotifyAt(DateHelper.formatLocalTimeToString(savedEntity.getNotifyAt()));
        }
    }

}
