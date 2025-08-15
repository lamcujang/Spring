package com.dbiz.app.userservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "d_leave_application", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplication  extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_leave_application_sq")
    @SequenceGenerator(name = "d_leave_application_sq", sequenceName = "d_leave_application_sq", allocationSize = 1)
    @Column(name = "d_leave_application_id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_timesheet_summary_id")
    private Integer timesheetSummaryId;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "approver_id")
    private Integer approverId;

    @Column(name = "reason")
    private String reason;
}
