package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "d_employee_attendance", schema = "pos")
public class EmployeeAttendance extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_employee_attendance_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_employee_attendance_sq")
    @SequenceGenerator(name = "d_employee_attendance_sq", sequenceName = "d_employee_attendance_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Column(name = "d_employee_id")
    private Integer employeeId;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "checkin_time")
    private LocalTime checkinTime;

    @Column(name = "checkin_reason")
    @Type(type = "org.hibernate.type.TextType")
    private String checkinReason;

    @Column(name = "d_config_shift_id")
    private Integer configShiftId;

    @Column(name = "attendance_type")
    @Type(type = "org.hibernate.type.TextType")
    private String attendanceType;

    @Column(name = "approved_by")
    private Integer approvedBy;

}