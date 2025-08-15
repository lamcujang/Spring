package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_pos_taxline", schema = "pos")
public class PosTaxLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_taxline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_taxline_sq")
    @SequenceGenerator(name = "d_pos_taxline_sq", sequenceName = "d_pos_taxline_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_pos_order_id", nullable = false, precision = 10)
    private Integer posOrderId;

    @NotNull
    @Column(name = "d_tax_id", nullable = false, precision = 10)
    private Integer taxId;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "tax_base_amount")
    private BigDecimal taxBaseAmount;

    @Size(max = 1)
    @Column(name = "is_price_intax", length = 1)
    private String isPriceInTax;

    @Size(max = 36)
    @Column(name = "d_pos_taxline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPosTaxlineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}