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
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_purchase_order", schema = "pos")
public class PurchaseOrder extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_purchase_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_purchase_order_sq")
    @SequenceGenerator(name = "d_purchase_order_sq", sequenceName = "d_purchase_order_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    private Integer doctypeId;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "d_vendor_id", precision = 10)
    private Integer vendorId;

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Size(max = 32)
    @NotNull
    @Column(name = "documentno", nullable = false, length = 32)
    private String documentNo;

    @Size(max = 3)
    @NotNull
    @Column(name = "order_status", nullable = false, length = 3)
    private String orderStatus;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_purchase_order_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dPurchaseOrderUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "discount_percent")
    private Float discountPercent;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "d_locator_id")
    private Integer locatorId;

}