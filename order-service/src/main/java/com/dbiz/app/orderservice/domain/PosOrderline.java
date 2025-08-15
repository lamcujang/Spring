package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_pos_orderline", schema = "pos")
public class PosOrderline extends AbstractMappedEntity implements Serializable {
    @Id

    @Column(name = "d_pos_orderline_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_orderline_sq")
    @SequenceGenerator(name = "d_pos_orderline_sq", sequenceName = "d_pos_orderline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "qty", nullable = false)
    private BigDecimal qty;

    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_pos_orderline_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String posOrderlineUu;

    @Column(name = "d_pos_order_id")
    private Integer posOrderId;

    @Column(name = "d_tax_id")
    private Integer taxId;

    @Column(name = "salesprice")
    private BigDecimal salesPrice;
    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "discount_percent", precision = 10)
    private BigDecimal discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "linenet_amt", nullable = false)
    private BigDecimal lineNetAmt;

    @Column(name = "grand_total", nullable = false)
    private BigDecimal grandTotal;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_production_id", precision = 10)
    private Integer productionId;

    @Column(name = "d_cancel_reason_id")
    private Integer cancelReasonId;

    @Column(name = "cancel_reason_message")
    private String cancelReasonMessage;

    @Column(name = "parentid")
    private Integer parentId;

    @Column(name = "d_kitchen_orderline_id")
    private Integer kitchenOrderLineId;

    @Column(name = "d_request_orderline_id")
    private Integer requestOrderLineId;

    @Column(name = "price_discount")
    private BigDecimal priceDiscount;

}