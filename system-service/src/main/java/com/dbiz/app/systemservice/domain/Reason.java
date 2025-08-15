package com.dbiz.app.systemservice.domain;

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
@Table(name = "d_reason", schema = "pos")
public class Reason extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reason_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reason_sq")
    @SequenceGenerator(name = "d_reason_sq", sequenceName = "d_reason_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @NotNull
    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;


    @Size(max = 500)
    @Column(name = "name", length = 500)
    private String name;

    @Size(max = 500)
    @Column(name = "type", length = 500)
    private String type;


}