package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;


// cai dat cham cong
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_config_time_keeping", schema = "pos")
public class ConfigTimeKeeping extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_config_time_keeping_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_config_time_keeping_sq")
    @SequenceGenerator(name = "d_config_time_keeping_sq", sequenceName = "d_config_time_keeping_sq", allocationSize = 1)

    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "standard_work_hours")
    private BigDecimal standardWorkHours;

    @Column(name = "max_lunch_break_minutes")
    private BigDecimal maxLunchBreakMinutes;

    @Column(name = "max_late_minutes")
    private BigDecimal maxLateMinutes;

    @Column(name = "max_early_leave_minutes")
    private BigDecimal maxEarlyLeaveMinutes;

    @Size(max = 1)
    @Column(name = "auto_mark_absent_if_no_checkin", length = 1)
    private String autoMarkAbsentIfNoCheckin;

    @Size(max = 1)
    @Column(name = "allow_checkout_after_work_hours", length = 1)
    private String allowCheckoutAfterWorkHours;

    @Size(max = 1)
    @Column(name = "require_reason_for_late_checkin", length = 1)
    private String requireReasonForLateCheckin;

    @Size(max = 1)
    @Column(name = "allow_supplement_request", length = 1)
    private String allowSupplementRequest;

    @Column(name = "request_deadline_days")
    private BigDecimal requestDeadlineDays;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "max_checkin_attempts_per_day")
    private BigDecimal maxCheckinAttemptsPerDay;

    @Size(max = 1)
    @Column(name = "mark_unpaid_leave_if_no_checkin_and_no_request", length = 1)
    private String markUnpaidLeaveIfNoCheckinAndNoRequest;

    @Size(max = 1)
    @Column(name = "no_fault_if_leave_request_exists", length = 1)
    private String noFaultIfLeaveRequestExists;

    @Size(max = 1)
    @Column(name = "allow_single_checkin_for_multiple_shifts", length = 1)
    private String allowSingleCheckinForMultipleShifts;

    @Size(max = 1)
    @Column(name = "auto_sum_working_hours_between_shifts", length = 1)
    private String autoSumWorkingHoursBetweenShifts;

    @Column(name = "merge_shifts_within_minutes")
    private BigDecimal mergeShiftsWithinMinutes;

    @Column(name = "max_shifts_per_day")
    private BigDecimal maxShiftsPerDay;

    @Column(name = "min_rest_between_shifts_minutes")
    private BigDecimal minRestBetweenShiftsMinutes;

    @Size(max = 1)
    @Column(name = "notify_if_no_checkin_2_days", length = 1)
    private String notifyIfNoCheckin2Days;

    @Size(max = 1)
    @Column(name = "warn_if_late_3_times_per_week", length = 1)
    private String warnIfLate3TimesPerWeek;

    @Size(max = 1)
    @Column(name = "warn_if_early_leave_3_times_per_week", length = 1)
    private String warnIfEarlyLeave3TimesPerWeek;

    @Column(name = "notify_at")
    private LocalTime notifyAt;

    @Column(name = "warning_recipient_id")
    private Integer warningRecipientId; // nguoi nhan canh boa

    @Size(max = 1)
    @Column(name = "notify_by_email", length = 1)
    private String notifyByEmail;

    @Size(max = 1)
    @Column(name = "notify_by_sms", length = 1)
    private String notifyBySms;

    @Size(max = 1)
    @Column(name = "notify_in_app", length = 1)
    private String notifyInApp;

}