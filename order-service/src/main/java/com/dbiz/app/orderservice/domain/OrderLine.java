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
@Table(name = "d_orderline", schema = "pos")
public class OrderLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_orderline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_orderline_sq")
    @SequenceGenerator(name = "d_orderline_sq", sequenceName = "d_orderline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_order_id")
    private Integer orderId;

    @NotNull
    @Column(name = "qty", nullable = false)
    private BigDecimal qty;

    @NotNull
    @Column(name = "price_entered", nullable = false)
    private BigDecimal priceEntered;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;


    @Column(name = "discount_percent", precision = 10)
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Size(max = 36)
    @Column(name = "d_orderline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dOrderlineUu;

    @Column(name = "d_tenant_id" )
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;
}