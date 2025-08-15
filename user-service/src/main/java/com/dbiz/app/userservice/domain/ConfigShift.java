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
@Table(name = "d_config_shift", schema = "pos")
public class ConfigShift extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_config_shift_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_config_shift_sq")
    @SequenceGenerator(name = "d_config_shift_sq", sequenceName = "d_config_shift_sq", allocationSize = 1)
    private Integer id;
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 50)
    @Column(name = "shift_type", length = 50)
    private String shiftType;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "checkin_time")
    private LocalTime checkinTime;

    @Column(name = "checkout_time")
    private LocalTime checkoutTime;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Size(max = 50)
    @Column(name = "working_days", length = 50)
    private String workingDays;



    @Size(max = 1)
    @Column(name = "is_valid_to", length = 1)
    private String isValidTo;

    @Column(name = "d_employee_created_by" )
    private Integer employeeCreatedBy;
}