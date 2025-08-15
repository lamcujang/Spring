package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_cancel_reason", schema = "pos")
public class CancelReason extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_cancel_reason_sq")
    @SequenceGenerator(name = "d_cancel_reason_sq", sequenceName = "d_cancel_reason_sq", allocationSize = 1)
    @Column(name = "d_cancel_reason_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 64)
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_cancel_reason_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dCancelReasonUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}