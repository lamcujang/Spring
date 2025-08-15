package com.dbiz.app.userservice.service.impl;

import com.dbiz.app.tenantservice.common.QueryEngine;
import com.dbiz.app.userservice.domain.Bonus;
import com.dbiz.app.userservice.domain.SalaryConfig;
import com.dbiz.app.userservice.repository.SalaryConfigRepository;
import com.dbiz.app.userservice.service.SalaryConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.orderDto.dtoView.ROHeaderVDto;
import org.common.dbiz.dto.userDto.AllowanceDto;
import org.common.dbiz.dto.userDto.BonusDto;
import org.common.dbiz.dto.userDto.PenaltyDeductionDto;
import org.common.dbiz.dto.userDto.SalaryConfigDto;
import org.common.dbiz.dto.userDto.nested.EmployeeDto;
import org.common.dbiz.payload.GlobalReponse;
import org.common.dbiz.payload.GlobalReponsePagination;
import org.common.dbiz.request.userRequest.SalaryConfigQueryRequest;
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
public class SalaryConfigServiceImpl implements SalaryConfigService {
    private final SalaryConfigRepository salaryConfigRepository;

    private final ModelMapper modelMapper;

    private final QueryEngine queryEngine;

    private final MessageSource messageSource;

    private final ObjectMapper mapper ;
    /**
     *
     * @param request
     * @return
     */
    @Override
    public GlobalReponsePagination findAll(SalaryConfigQueryRequest request) {
        Parameter parameter = new Parameter();
        if(request.getId() != null)
        {
            parameter.add("d_salary_config_id", request.getId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        }
        if(request.getOrgId() != null)
            parameter.add("d_org_id", request.getOrgId(), Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        ResultSet rs = queryEngine.getRecords("d_salary_config_get_v",
                parameter, request);
        List<SalaryConfigDto> listResponse = new ArrayList<>();
        try {
            while (rs.next()) {
                SalaryConfigDto dto = SalaryConfigDto.builder()
                        .id(rs.getInt("d_salary_config_id"))
                        .orgId(rs.getInt("d_org_id"))
                        .otLimitPerDay(rs.getBigDecimal("ot_limit_per_day"))
                        .requireOtApprovalBeforeWork(rs.getString("require_ot_approval_before_work"))
                        .otCoefficientWeekday(rs.getBigDecimal("ot_coefficient_weekday"))
                        .otCoefficientWeekend(rs.getBigDecimal("ot_coefficient_weekend"))
                        .autoOtWeekend(rs.getString("auto_ot_weekend"))
                        .autoOtHoliday(rs.getString("auto_ot_holiday"))
                        .otCoefficientHoliday(rs.getBigDecimal("ot_coefficient_holiday"))
                        .applyFrom(rs.getBigDecimal("apply_from"))
                        .applyTo(rs.getBigDecimal("apply_to"))
                        .otCoefficientNight(rs.getBigDecimal("ot_coefficient_night"))
                        .notifyOtRequest(rs.getString("notify_ot_request"))
                        .autoRejectUnapprovedOt(rs.getString("auto_reject_unapproved_ot"))
                        .totalWorkHoursLt(rs.getBigDecimal("total_work_hours_lt"))
                        .underworkCoefficientLt(rs.getBigDecimal("underwork_coefficient_lt"))
                        .totalWorkHoursFrom(rs.getBigDecimal("total_work_hours_from"))
                        .totalWorkHoursTo(rs.getBigDecimal("total_work_hours_to"))
                        .underworkCoefficientTo(rs.getBigDecimal("underwork_coefficient_to"))
                        .excludeLunchBreakFromUnderwork(rs.getString("exclude_lunch_break_from_underwork"))
                        .roundUnderworkTime15min(rs.getString("round_underwork_time_15min"))
                        .allowUnderworkOffsetByOt(rs.getString("allow_underwork_offset_by_ot"))
                        .socialInsuranceEmployeeRate(rs.getBigDecimal("social_insurance_employee_rate"))
                        .healthInsuranceEmployeeRate(rs.getBigDecimal("health_insurance_employee_rate"))
                        .accidentInsuranceEmployeeRate(rs.getBigDecimal("accident_insurance_employee_rate"))
                        .socialInsuranceCompanyRate(rs.getBigDecimal("social_insurance_company_rate"))
                        .healthInsuranceCompanyRate(rs.getBigDecimal("health_insurance_company_rate"))
                        .accidentInsuranceCompanyRate(rs.getBigDecimal("accident_insurance_company_rate"))
                        .laborAccidentInsurance(rs.getBigDecimal("labor_accident_insurance"))
                        .minInsuranceContribution(rs.getBigDecimal("min_insurance_contribution"))
                        .maxInsuranceContribution(rs.getBigDecimal("max_insurance_contribution"))
                        .autoCalculateEndMonthSalary(rs.getString("auto_calculate_end_month_salary"))
                        .autoSendPayslipEmail(rs.getString("auto_send_payslip_email"))
                        .autoBackupData(rs.getString("auto_backup_data"))
                        .remindClosingBeforeSalaryCalc(rs.getString("remind_closing_before_salary_calc"))
                        .alertExceedOtLimit(rs.getString("alert_exceed_ot_limit"))
                        .notifyAbsence(rs.getString("notify_absence"))
                        .alertSalaryNotCalculated(rs.getString("alert_salary_not_calculated"))
                        .alertAttendanceNotClosed(rs.getString("alert_attendance_not_closed"))
                        .salaryCycle(rs.getString("salary_cycle"))
                        .salaryCalcDay(rs.getString("salary_calc_day"))
                        .notificationEmail(rs.getString("notification_email"))
                        .autoApprovalLimit(rs.getBigDecimal("auto_approval_limit"))
                        .notifyEmailOnReward(rs.getString("notify_email_on_reward"))
                        .logRewardHistory(rs.getString("log_reward_history"))
                        .dailyApprovalReport(rs.getString("daily_approval_report"))
//                        .otApprover(rs.getInt("ot_approver"))
//                        .rewardApprover(rs.getInt("reward_approver"))
                        .build();
                org.common.dbiz.dto.userDto.nested.EmployeeDto otApprover = EmployeeDto.builder()
                        .id(rs.getInt("ot_approver")).name(rs.getString("ot_approver_name"))
                        .build();
                EmployeeDto rewardApprover = EmployeeDto.builder()
                        .id(rs.getInt("reward_approver")).name(rs.getString("reward_approver_name"))
                        .build();
                dto.setOtApproverDto(otApprover);
                dto.setRewardApproverDto(rewardApprover);
                dto.setPenaltyDeductions(getPenaltyDeduction(rs.getInt("d_salary_config_id")));
                dto.setBonuses(getBonus(rs.getInt("d_salary_config_id")));
                dto.setAllowances(getAllowances(rs.getInt("d_salary_config_id")));
                listResponse.add(dto);
            }
        }catch (Exception e)
        {
            throw new RuntimeException("Error while fetching SalaryConfig data: " + e.getMessage(), e);
        }
        Pagination pagination = queryEngine.getPagination("d_salary_config_get_v", parameter, request);
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

    private List<PenaltyDeductionDto> getPenaltyDeduction(Integer salaryConfigId){
        Parameter parameter = new Parameter();
        parameter.add("d_salary_config_id", salaryConfigId, Param.Logical.EQUAL, Param.Relational.NONE, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_penalty_deduction_v", parameter);
        List<PenaltyDeductionDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                PenaltyDeductionDto penaltyDeductionDto = PenaltyDeductionDto.builder()
                        .id(rs.getInt("d_penalty_deduction_id"))
                        .code(rs.getString("code"))
                        .name(rs.getString("penalty_deduction_name"))
                        .description(rs.getString("description"))
                        .penaltyAmount(rs.getBigDecimal("penalty_amount"))
                        .warningCount(rs.getInt("warning_count"))
                        .value(rs.getString("value"))
                        .nameValue(rs.getString("name"))
                        .salaryConfigId(rs.getInt("d_salary_config_id"))
                        .build();
                data.add(penaltyDeductionDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    private List<BonusDto> getBonus(Integer salaryConfigId){
        Parameter parameter = new Parameter();
        parameter.add("d_salary_config_id", salaryConfigId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_bonus_id", null, Param.Logical.IS_NOT_NULL, Param.Relational.NONE, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_config_salary_bonus_allowances_v", parameter);
        List<BonusDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                BonusDto penaltyDeductionDto = BonusDto.builder()
                        .id(rs.getInt("d_bonus_id"))
                        .name(rs.getString("bonus_name"))
                        .description(rs.getString("description"))
                        .price(rs.getBigDecimal("bonus_price"))
                        .bonusGroup(rs.getString("bonus_group"))
                        .kpiBonus(rs.getString("kpi_bonus"))
                        .holidayBonus(rs.getString("holiday_bonus"))
                        .performanceBonus(rs.getString("performance_bonus"))
                        .build();
                data.add(penaltyDeductionDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    private List<AllowanceDto> getAllowances(Integer salaryConfigId){
        Parameter parameter = new Parameter();
        parameter.add("d_salary_config_id", salaryConfigId, Param.Logical.EQUAL, Param.Relational.AND, Param.NONE);
        parameter.add("d_allowance_id", null, Param.Logical.IS_NOT_NULL, Param.Relational.NONE, Param.NONE);
        ResultSet rs = queryEngine.getRecordsWithoutPaging("pos.d_config_salary_bonus_allowances_v", parameter);
        List<AllowanceDto> data = new ArrayList<>();
        try {
            while (rs.next()) {
                AllowanceDto penaltyDeductionDto = AllowanceDto.builder()
                        .id(rs.getInt("d_bonus_id"))
                        .name(rs.getString("bonus_name"))
                        .price(rs.getBigDecimal("bonus_price"))
                        .build();

                data.add(penaltyDeductionDto);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
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
    @Override
    public GlobalReponse save(SalaryConfigDto dto) {
        GlobalReponse response = new GlobalReponse();
        SalaryConfig savedEntity;


        if (dto.getId() != null) {
            // UPDATE
            SalaryConfig existingEntity = salaryConfigRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("SalaryConfig not found with id: " + dto.getId()));
            modelMapper.map(dto, existingEntity);
            mapApprovers(dto, existingEntity);
            savedEntity = salaryConfigRepository.save(existingEntity);
            response.setMessage("SalaryConfig updated successfully");
            response.setStatus(HttpStatus.OK.value());
        } else {
            // CREATE
            SalaryConfig newEntity = modelMapper.map(dto, SalaryConfig.class);
            newEntity.setTenantId(0); // TODO: lấy tenant từ context nếu cần
            newEntity.setOrgId(dto.getOrgId());
            mapApprovers(dto, newEntity);
            savedEntity = salaryConfigRepository.save(newEntity);
            response.setMessage("SalaryConfig created successfully");
            response.setStatus(HttpStatus.CREATED.value());
        }

        // Trả kết quả DTO
        SalaryConfigDto responseDto = modelMapper.map(savedEntity, SalaryConfigDto.class);
        responseDto.setOtApproverDto(dto.getOtApproverDto());
        responseDto.setRewardApproverDto(dto.getRewardApproverDto());

        response.setData(responseDto);
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


    private void mapApprovers(SalaryConfigDto dto, SalaryConfig entity) {
        if (dto.getOtApproverDto() != null && dto.getOtApproverDto().getId() != null) {
            entity.setOtApproverId(dto.getOtApproverDto().getId());
        }
        if (dto.getRewardApproverDto() != null && dto.getRewardApproverDto().getId() != null) {
            entity.setRewardApproverId(dto.getRewardApproverDto().getId());
        }
    }
}
