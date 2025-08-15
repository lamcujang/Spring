package com.dbiz.app.userservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_attendance_request", schema = "pos")
public class AttendanceRequest {


    @Id
    @Column(name = "d_attendance_request_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_attendance_request_sq")
    @SequenceGenerator(name = "d_attendance_request_sq", sequenceName = "d_attendance_request_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Column(name = "d_employee_id", nullable = false)
    private Integer employeeId;

    @Size(max = 50)
    @Column(name = "time_keeping_type", length = 50)
    private String timeKeepingType;

    @NotNull
    @Column(name = "d_config_shift_id", nullable = false)
    private Integer configShiftId;

    @Size(max = 255)
    @Column(name = "reason_request")
    private String reasonRequest;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;


}