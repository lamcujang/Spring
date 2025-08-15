package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDetailDto;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_kitchen_orderline", schema = "pos")
public class KitchenOrderLine extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_kitchen_orderline_sq")
    @SequenceGenerator(name = "d_kitchen_orderline_sq", sequenceName = "d_kitchen_orderline_sq", allocationSize = 1)
    @Column(name = "d_kitchen_orderline_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_kitchen_order_id", nullable = false, precision = 10)
    private Integer kitchenOrderId;


    @Size(max = 5)
    @Column(name = "orderline_status", length = 5)
    private String orderlineStatus;

    @Column(name = "d_product_id", nullable = false, precision = 10)
    private Integer productId;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @NotNull
    @Column(name = "qty", nullable = false)
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
    @Column(name = "d_kitchen_orderline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String kitchenOrderlineUu;

    @Column(name = "d_cancel_reason_id", precision = 10)
    private Integer cancelReasonId;

    @Column(name = "d_pos_orderline_id")
    private Integer posOrderLineId;

    @Column(name = "d_production_id", precision = 10)
    private Integer productionId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Transient
    private List<KitchenOrderlineDetailDto> lineDetails;
}