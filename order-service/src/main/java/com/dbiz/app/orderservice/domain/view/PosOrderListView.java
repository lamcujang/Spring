package com.dbiz.app.orderservice.domain.view;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_posorder_list_v", schema = "pos")
public class PosOrderListView extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;


    @Size(max = 32)
    @Column(name = "document_no", length = 32)
    private String documentNo;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;


    @Column(name = "order_guests")
    private Integer orderGuests;

    @Size(max = 64)
    @Column(name = "order_status", length = 64)
    private String orderStatus;

    @Size(max = 15)
    @Column(name = "order_status_name", length = 15)
    private String orderStatusName;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "d_customer_id", precision = 10)
    private BigDecimal customerId;

    @Column(name = "paid")
    private BigDecimal paid;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "d_user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "d_pricelist_id")
    private Integer priceListId;

    @Column(name = "pricelist_name")
    private String priceListName;

    @Column(name = "description")
    private String description;

    @Column(name = "d_shift_control_id")
    private Integer shiftControlId;

    @Column(name = "price_category_code")
    private String priceCateCode;
}