package com.dbiz.app.orderservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_wds_detail_history_v", schema = "pos")
public class WDSDetailHistory extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_kitchen_orderline_id", precision = 10)
    private Integer id;


    @Column(name = "d_kitchen_order_id", precision = 10)
    private Integer kitchenOrderId;

    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "qty")
    private Integer qty;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @Size(max = 64)
    @Column(name = "name_status", length = 64)
    private String nameStatus;

    @Size(max = 5)
    @Column(name = "orderline_status", length = 5)
    private String orderLineStatus;

    @Size(max = 255)
    @Column(name = "product_name")
    private String productName;


    @Size(max = 128)
    @Column(name = "pos_name", length = 128)
    private String posName;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Column(name = "waiting_time")
    private Integer waitingTime;

    @Column(name = "completed_time")
    private Integer completedTime;

    @Size(max = 32)
    @Column(name = "documentno", length = 32)
    private String documentNo;

    @Column(name = "dateordered")
    private Instant dateOrdered;

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Column(name="cancel_reason")
    private String cancelReason;

    @Column(name="d_cancel_reason_id")
    private Integer cancelReasonId;
}
