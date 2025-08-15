package com.dbiz.app.orderservice.domain.view;

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
@Table(name = "d_kitchen_orderline_v", schema = "pos")
public class KitchenOrderLineView  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_kitchen_orderline_id", precision = 10)
    private Integer kitchenOrderLineId;

    @Column(name = "d_kitchen_order_id", precision = 10)
    private Integer kitchenOrderId;


    @Size(max = 5)
    @Column(name = "order_status_value", length = 5)
    private String orderStatusValue;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "transfer_qty")
    private BigDecimal transferQty;

    @Column(name = "cancel_qty")
    private BigDecimal cancelQty;

    @Size(max = 5)
    @Column(name = "priority", length = 5)
    private String priority;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Size(max = 36)
    @Column(name = "d_kitchen_orderline_uu", length = 36)
    private String kitchenOrderLineUu;

    @Column(name = "d_cancel_reason_id", precision = 10)
    private Integer cancelReasonId;

    @Size(max = 64)
    @Column(name = "cancel_reason", length = 64)
    private String cancelReason;

    @Size(max = 64)
    @Column(name = "order_status", length = 64)
    private String orderStatus;

    @Column(name = "d_pos_orderline_id", precision = 10)
    private Integer posOrderLineId;

    @Column(name = "d_production_id", precision = 10)
    private Integer productionId;

    @Embedded
    KitchenLineProductView product;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 15)
    @Column(name = "uom_name", length = 15)
    private String uomName;

    @Size(max = 5)
    @Column(name = "uom_code", length = 5)
    private String uomCode;


    @Size(max = 32)
    @Column(name = "production_no", length = 32)
    private String productionNo;

    @Column(name = "parent_id")
    private Integer parentId;

}