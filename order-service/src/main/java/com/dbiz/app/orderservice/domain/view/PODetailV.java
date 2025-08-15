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
@Table(name = "d_po_detail_v")
public class PODetailV extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_purchase_orderline_id", precision = 10)
    private Integer id;

    @Column(name = "d_purchase_order_id", precision = 10)
    private Integer purchaseOrderId;

    @Column(name = "qty")
    private BigDecimal qty;

    @Column(name = "priceentered")
    private BigDecimal priceEntered;

    @Column(name = "net_amount")
    private BigDecimal netAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Embedded
    ProductPOV productDto;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_tax_id")
    private Integer taxId;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_name")
    private String taxName;

    @Column(name="total_discount_amount")
    BigDecimal totalDiscountAmount;

    @Column(name="d_uom_id")
    Integer uomId;

    @Column(name="uom_code")
    String code;

    @Column(name="uom_name")
    String unitOfMeasure;

    @Column(name = "discount_percent")
    BigDecimal discountPercent;

    @Column(name = "discount_amount")
    BigDecimal discountAmount;

    @Column(name = "price_discount")
    BigDecimal priceDiscount;

    @Column(name = "d_lot_id")
    Integer lotId;

    @Column(name = "lot_code")
    String lotCode;

    @Column(name = "expiry_date")
    String expiryDate;
}