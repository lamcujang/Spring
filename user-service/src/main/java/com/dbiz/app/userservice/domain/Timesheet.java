package com.dbiz.app.userservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "d_timesheet", schema = "pos")
public class Timesheet {



    @Id
    @Column(name = "d_timesheet_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_timesheet_sq")
    @SequenceGenerator(name = "d_timesheet_sq", sequenceName = "d_timesheet_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @NotNull
    @Column(name = "d_employee_id", nullable = false)
    private Integer employeeId;

    @NotNull
    @Column(name = "d_config_shift_id", nullable = false)
    private Integer configShiftId;

    @Size(max = 255)
    @Column(name = "work_type")
    private String workType;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 25)
    @Column(name = "checkin_source", length = 25)
    private String checkinSource;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

}