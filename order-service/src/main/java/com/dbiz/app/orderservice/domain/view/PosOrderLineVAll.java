package com.dbiz.app.orderservice.domain.view;

import com.dbiz.app.orderservice.domain.PosOrder;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_pos_orderline_v_all", schema = "pos")
public class PosOrderLineVAll extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_orderline_id", precision = 10)
    private Integer id;


    @Column(name = "salesprice")
    private BigDecimal salesPrice;

    @Column(name = "qty")
    private BigDecimal qty;

    @Size(max = 64)
    @Column(name = "status", length = 64)
    private String status;

    @Size(max = 15)
    @Column(name = "value_status", length = 15)
    private String valueStatus;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "product_id", precision = 10)
    private Integer productId;

    @Embedded
    ProductV productDto;

    @Embedded
    UomV uomDto;

    @ManyToOne
    @JoinColumn(name = "d_pos_order_id", insertable = false, updatable = false)
    private PosOrder posOrder;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "linenet_amt")
    private BigDecimal lineNetAmt;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "parentid")
    private Integer parentId;


    @Column(name = "d_kitchen_orderline_id", precision = 10)
    private Integer kitchenOrderLineId;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_name")
    private String taxName;
}