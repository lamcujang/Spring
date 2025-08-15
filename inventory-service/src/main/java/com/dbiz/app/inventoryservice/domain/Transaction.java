package com.dbiz.app.inventoryservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_transaction", schema = "pos")
public class Transaction extends AbstractMappedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_transaction_sq")
    @SequenceGenerator(name = "d_transaction_sq", sequenceName = "d_transaction_sq", allocationSize = 1)
    @Column(name = "d_transaction_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "d_warehouse_id")
    private Integer warehouseId;

    @Column(name = "d_locator_id")
    private Integer locatorId;

    @Size(max = 36)
    @Column(name = "d_transaction_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String transactionUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "d_purchase_orderline_id")
    private Integer purchaseOrderLineId;

    @Column(name = "d_pos_orderline_id")
    private Integer posOrderLineId;

    @Column(name = "d_productionline_id")
    private Integer productionLineId;

    @Column(name = "d_kitchen_orderline_id")
    private Integer kitchenOrderLineId;

    @Column(name = "d_lot_id")
    private Integer lotId;

    @Column(name = "d_return_orderline_id")
    private Integer returnOrderLineId;

}
