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
@Table(name = "d_reservation_v_all", schema = "pos")
public class ReservationVAll  extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_reservation_order_id", precision = 10)
    private Integer id;


    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "total_cus")
    private BigDecimal totalCus;

    @Size(max = 255)
    @Column(name = "company")
    private String company;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "reservation_time")
    private Instant reservationTime;

    @Column(name = "reser_amount")
    private BigDecimal reserAmount;

    @Size(max = 10)
    @Column(name = "status", length = 10)
    private String status;

    @Size(max = 36)
    @Column(name = "d_reservation_order_uu", length = 36)
    private String dReservationOrderUu;

    @Size(max = 20)
    @Column(name = "time_tocome", length = 20)
    private String timeTocome;

    @Column(name = "qty_adult")
    private BigDecimal qtyAdult;

    @Column(name = "qty_baby")
    private BigDecimal qtyBaby;

    @Embedded
    TableReservationVAll table;

    @Embedded
    FloorReservationVAll floor;

    @Embedded
    CustomerReservationVAll customer;

    @Embedded
    UserReservationVAll user;

    @Embedded
    ReferenceReservationVAll reservationStatus;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;
}