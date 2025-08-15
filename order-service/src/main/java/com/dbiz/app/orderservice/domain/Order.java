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
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "d_order", schema = "pos")
public class Order extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_order_sq")
    @SequenceGenerator(name = "d_order_sq", sequenceName = "d_order_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @Column(name = "document_no", length = 32)
    private String documentNo;

    @NotNull
    @Column(name = "d_customer_id", nullable = false, precision = 10)
    private BigDecimal customerId;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 5)
    @NotNull
    @Column(name = "order_status", nullable = false, length = 5)
    private String orderStatus;

    @Size(max = 10)
    @NotNull
    @Column(name = "source", nullable = false, length = 10)
    private String source;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_locked", length = 1)
    private String isLocked;

    @NotNull
    @Column(name = "d_table_id", nullable = false, precision = 10)
    private Integer tableId;

    @NotNull
    @Column(name = "d_floor_id", nullable = false, precision = 10)
    private Integer floorId;

    @NotNull
    @Column(name = "d_user_id", nullable = false, precision = 10)
    private Integer userId;

    @Column(name = "order_guests")
    private Integer orderGuests;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;


    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @NotNull
    @Column(name = "d_currency_id", nullable = false, precision = 10)
    private Integer currencyId;

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer priceListId;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Size(max = 5)
    @Column(name = "payment_method", length = 5)
    private String paymentMethod;

    @NotNull
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    private int docTypeId;

    @Size(max = 36)
    @Column(name = "d_order_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dOrderUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;
}