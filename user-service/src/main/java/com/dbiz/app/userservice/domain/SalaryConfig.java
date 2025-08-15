package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_salary_config", schema = "pos")
public class SalaryConfig extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_salary_config_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_salary_config_sq")
    @SequenceGenerator(name = "d_salary_config_sq", sequenceName = "d_salary_config_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Column(name = "ot_limit_per_day")
    private BigDecimal otLimitPerDay;

    @Size(max = 1)
    @Column(name = "require_ot_approval_before_work", length = 1)
    private String requireOtApprovalBeforeWork;

    @Column(name = "ot_coefficient_weekday")
    private BigDecimal otCoefficientWeekday;

    @Column(name = "ot_coefficient_weekend")
    private BigDecimal otCoefficientWeekend;

    @Size(max = 1)
    @Column(name = "auto_ot_weekend", length = 1)
    private String autoOtWeekend;

    @Size(max = 1)
    @Column(name = "auto_ot_holiday", length = 1)
    private String autoOtHoliday;

    @Column(name = "ot_coefficient_holiday")
    private BigDecimal otCoefficientHoliday;

    @Column(name = "apply_from")
    private BigDecimal applyFrom;

    @Column(name = "apply_to")
    private BigDecimal applyTo;

    @Column(name = "ot_coefficient_night")
    private BigDecimal otCoefficientNight;

    @Column(name = "ot_approver")
    private Integer otApproverId;

    @Size(max = 1)
    @Column(name = "notify_ot_request", length = 1)
    private String notifyOtRequest;

    @Size(max = 1)
    @Column(name = "auto_reject_unapproved_ot", length = 1)
    private String autoRejectUnapprovedOt;

    @Column(name = "total_work_hours_lt")
    private BigDecimal totalWorkHoursLt;

    @Column(name = "underwork_coefficient_lt")
    private BigDecimal underworkCoefficientLt;

    @Column(name = "total_work_hours_from")
    private BigDecimal totalWorkHoursFrom;

    @Column(name = "total_work_hours_to")
    private BigDecimal totalWorkHoursTo;

    @Column(name = "underwork_coefficient_to")
    private BigDecimal underworkCoefficientTo;

    @Size(max = 1)
    @Column(name = "exclude_lunch_break_from_underwork", length = 1)
    private String excludeLunchBreakFromUnderwork;

    @Size(max = 1)
    @Column(name = "round_underwork_time_15min", length = 1)
    private String roundUnderworkTime15min;

    @Size(max = 1)
    @Column(name = "allow_underwork_offset_by_ot", length = 1)
    private String allowUnderworkOffsetByOt;

    @Column(name = "social_insurance_employee_rate")
    private BigDecimal socialInsuranceEmployeeRate;

    @Column(name = "health_insurance_employee_rate")
    private BigDecimal healthInsuranceEmployeeRate;

    @Column(name = "accident_insurance_employee_rate")
    private BigDecimal accidentInsuranceEmployeeRate;

    @Column(name = "social_insurance_company_rate")
    private BigDecimal socialInsuranceCompanyRate;

    @Column(name = "health_insurance_company_rate")
    private BigDecimal healthInsuranceCompanyRate;

    @Column(name = "accident_insurance_company_rate")
    private BigDecimal accidentInsuranceCompanyRate;

    @Column(name = "labor_accident_insurance")
    private BigDecimal laborAccidentInsurance;

    @Column(name = "min_insurance_contribution")
    private BigDecimal minInsuranceContribution;

    @Column(name = "max_insurance_contribution")
    private BigDecimal maxInsuranceContribution;

    @Size(max = 1)
    @Column(name = "auto_calculate_end_month_salary", length = 1)
    private String autoCalculateEndMonthSalary;

    @Size(max = 1)
    @Column(name = "auto_send_payslip_email", length = 1)
    private String autoSendPayslipEmail;

    @Size(max = 1)
    @Column(name = "auto_backup_data", length = 1)
    private String autoBackupData;

    @Size(max = 1)
    @Column(name = "remind_closing_before_salary_calc", length = 1)
    private String remindClosingBeforeSalaryCalc;

    @Size(max = 1)
    @Column(name = "alert_exceed_ot_limit", length = 1)
    private String alertExceedOtLimit;

    @Size(max = 1)
    @Column(name = "notify_absence", length = 1)
    private String notifyAbsence;

    @Size(max = 1)
    @Column(name = "alert_salary_not_calculated", length = 1)
    private String alertSalaryNotCalculated;

    @Size(max = 1)
    @Column(name = "alert_attendance_not_closed", length = 1)
    private String alertAttendanceNotClosed;

    @Size(max = 25)
    @Column(name = "salary_cycle", length = 25)
    private String salaryCycle;

    @Size(max = 25)
    @Column(name = "salary_calc_day", length = 25)
    private String salaryCalcDay;

    @Size(max = 25)
    @Column(name = "notification_email", length = 25)
    private String notificationEmail;

    @Column(name = "reward_approver")
    private Integer rewardApproverId;

    @Column(name = "auto_approval_limit")
    private BigDecimal autoApprovalLimit;

    @Size(max = 1)
    @Column(name = "notify_email_on_reward", length = 1)
    private String notifyEmailOnReward;

    @Size(max = 1)
    @Column(name = "log_reward_history", length = 1)
    private String logRewardHistory;

    @Size(max = 1)
    @Column(name = "daily_approval_report", length = 1)
    private String dailyApprovalReport;

}