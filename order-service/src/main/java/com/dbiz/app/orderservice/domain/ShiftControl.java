package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "d_shift_control", schema = "pos")
public class ShiftControl extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_shift_control_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_shift_control_sq")
    @SequenceGenerator(name = "d_shift_control_sq", sequenceName = "d_shift_control_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 36)
    @Column(name = "d_shift_control_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String shiftControlUu;


    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Size(max = 3)
    @Column(name = "shift_type", length = 3)
    private String shiftType;

    @Size(max = 1)
    @Column(name = "is_closed", nullable = false, length = 1)
    private String isClosed;

    @Column(name = "erp_shift_control_id", precision = 10)
    private Integer erpShiftControlId;

    @Column(name = "start_cash")
    private BigDecimal startCash;

    @Column(name = "transfer_cash")
    private BigDecimal transferCash;

    @Column(name = "cash_diff")
    private BigDecimal cashDiff;

    @Column(name = "d_doctype_id")
    private Integer doctypeId;

    @Size(max = 100)
    @Column(name = "document_no", length = 100)
    private String documentNo;


    @Size(max = 255)
    @Column(name = "descriptions")
    private String descriptions;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}