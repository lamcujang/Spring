package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.dbiz.app.tenantservice.domain.Org;
import com.dbiz.app.tenantservice.domain.Tenant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_currency", schema = "pos")
public class Currency extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_currency_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_currency_sq")
    @SequenceGenerator(name = "d_currency_sq", sequenceName = "d_currency_sq", allocationSize = 1)
    private Integer id;

     @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false)
    private Integer orgId;

    @Size(max = 3)
    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "standard_precision", nullable = false, precision = 2)
    private BigDecimal standardPrecision;


}