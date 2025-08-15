package com.dbiz.app.orderservice.domain;


import com.dbiz.app.orderservice.domain.view.ProductV;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_wds_detail_v", schema = "pos")
public class WDSDetail extends AbstractMappedEntity implements Serializable {


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

    @Size(max = 64)
    @Column(name = "cancelreason", length = 64)
    private String cancelreason;

    @Size(max = 64)
    @Column(name = "orderline_status", length = 64)
    private String orderLineStatus;

    @Embedded
    ProductV product;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "priority")
    private String priority;

    @Column(name = "priority_value")
    private String priorityValue;
}
