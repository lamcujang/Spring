package com.dbiz.app.orderservice.domain.view;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_po_header_v")
public class POHeaderV extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_purchase_order_id", precision = 10)
    private Integer id;

    @Size(max = 3)
    @Column(name = "order_status", length = 3)
    private String orderStatus;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 64)
    @Column(name = "name_reference", length = 64)
    private String nameReference;

    @Column(name = "order_date")
    private Instant orderDate;

    @Embedded
    UserPOV userDto;

    @Embedded
    VendorPOV vendorDto;

    @Embedded
    WarehousePOV warehouseDto;

    @Column(name = "totalproduct")
    private BigDecimal totalProduct;

    @Column(name = "totalqty")
    private BigDecimal totalQty;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "documentno")
    private String documentNo;

    @Column(name="discount_percent")
    private BigDecimal discountPercent;

    @Column(name="discount_amount")
    private BigDecimal discountAmount;

    @Column(name="net_amount")
    private BigDecimal netAmount;

    @Column(name="tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "description")
    String description;

}