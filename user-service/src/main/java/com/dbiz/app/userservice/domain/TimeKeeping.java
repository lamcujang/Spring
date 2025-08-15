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
@Table(name = "d_time_keeping", schema = "pos")
public class TimeKeeping extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_time_keeping_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_time_keeping_sq")
    @SequenceGenerator(name = "d_time_keeping_sq", sequenceName = "d_time_keeping_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Size(max = 500)
    @Column(name = "checkin_address", length = 500)
    private String checkinAddress;

    @Size(max = 500)
    @Column(name = "gps_coordinates", length = 500)
    private String gpsCoordinates;

    @Column(name = "checkin_radius_meters")
    private Integer checkinRadiusMeters;

    @Column(name = "updated_qr")
    private Instant updatedQr;

    @Column(name = "employee_created_by")
    private Integer employeeCreatedBy;
}