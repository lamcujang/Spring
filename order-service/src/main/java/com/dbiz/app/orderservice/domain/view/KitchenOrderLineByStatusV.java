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
@Table(name = "get_kitchen_order_bystatus_v", schema = "pos")
public class KitchenOrderLineByStatusV extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_kitchen_orderline_id", precision = 10)
    private Integer id;

    @Column(name = "d_kitchen_order_id", precision = 10)
    private Integer kitchenOrderId;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @Column(name = "qty")
    private BigDecimal qty;

    @Size(max = 5)
    @Column(name = "status_value", length = 5)
    private String statusValue;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "d_pos_orderline_id", precision = 10)
    private BigDecimal posOrderlineId;

    @Column(name = "d_production_id", precision = 10)
    private BigDecimal productionId;

    @Size(max = 32)
    @Column(name = "documentno", length = 32)
    private String productionDocNo;

    @Size(max = 64)
    @Column(name = "cancelreason", length = 64)
    private String cancelreason;

    @Size(max = 64)
    @Column(name = "orderline_status", length = 64)
    private String orderLineStatus;

   @Embedded
   ProductV product;

    @Size(max = 36)
    @Column(name = "d_kitchen_orderline_uu", length = 36)
    private String kitchenOrderLineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "cooking_time")
    private BigDecimal cookingTime;

    @Column(name = "preparation_time")
    private BigDecimal preparationTime;

    @Column(name = "priority")
    private String priority;

    @Column(name = "priority_value")
    private String priorityValue;

    @Column(name = "parent_id")
    private Integer parentId;

}