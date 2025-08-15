package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_timesheet_summary", schema = "pos")
public class TimeSheetSummary extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_timesheet_summary_sq")
    @SequenceGenerator(name = "d_timesheet_summary_sq", sequenceName = "d_timesheet_summary_sq", allocationSize = 1)
    @Column(name = "d_timesheet_summary_id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_employee_id")
    private Integer employeeId;

    @Column(name = "standard_working_hours")
    private BigDecimal standardWorkingHours;

    @Column(name = "total_working_hours")
    private BigDecimal totalWorkingHours;

    @Column(name = "actual_working_hours")
    private BigDecimal actualWorkingHours;

    @Column(name = "overtime_hours")
    private BigDecimal overtimeHours;

    @Column(name = "late_early_hours")
    private BigDecimal lateEarlyHours;

    @Column(name = "monthly_overtime_hours")
    private BigDecimal monthlyOvertimeHours;

    @Column(name = "annual_leave_used")
    private Integer annualLeaveUsed;

    @Column(name = "monthly_late_early_hours")
    private BigDecimal monthlyLateEarlyHours;
}
