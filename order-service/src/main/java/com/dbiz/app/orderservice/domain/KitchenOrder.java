package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "d_kitchen_order", schema = "pos")
public class KitchenOrder extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_kitchen_order_sq")
    @SequenceGenerator(name = "d_kitchen_order_sq", sequenceName = "d_kitchen_order_sq", allocationSize = 1)
    @Column(name = "d_kitchen_order_id", nullable = false, precision = 10)
    private Integer id;


    @Size(max = 32)
    @Column(name = "documentno", nullable = false, length = 32)
    private String documentno;

    @Column(name = "d_pos_order_id", nullable = false, precision = 10)
    private Integer posOrderId;

    @Column(name = "d_doctype_id", precision = 10)
    private Integer doctypeId;

    @Column(name = "d_warehouse_id", precision = 10)
        private Integer warehouseId;

    @Column(name = "dateordered", nullable = false)
    private Instant dateordered;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "d_floor_id", precision = 10)
    private Integer floorId;

    @Column(name = "d_table_id", precision = 10)
    private Integer tableId;

    @Size(max = 5)
    @Column(name = "order_status", length = 5)
    private String orderStatus;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "d_tenant_id" )
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "erp_kitchen_order_id")
    private Integer erpKitchenOrderId;

    @Column(name = "is_sync_erp")
    private String isSyncErp;



    @Size(max = 36)
    @Column(name = "d_kitchen_order_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String kitchenOrderUu;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

}