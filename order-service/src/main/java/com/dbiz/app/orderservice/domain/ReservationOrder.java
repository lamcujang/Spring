package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_reservation_order", schema = "pos")
public class ReservationOrder extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reservation_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reservation_order_sq")
    @SequenceGenerator(name = "d_reservation_order_sq", sequenceName = "d_reservation_order_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "d_floor_id")
    private Integer floorId;

    @Column(name = "d_table_id")
    private Integer tableId;

    @Size(max = 255)
    @Column(name = "customer_name")
        private String customerName;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 255)
    @Column(name = "company")
    private String company;

    @Column(name = "total_cus")
    private BigDecimal totalCus;


    @Column(name = "reser_amount")
    private BigDecimal reserAmount;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;


    @Size(max = 36)
    @Column(name = "d_reservation_order_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String reservationOrderUu;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Column(name = "d_customer_id", precision = 10)
    private Integer customerId;

    @Size(max = 20)
    @Column(name = "time_tocome", length = 20)
    private String timeTocome;

    @Column(name = "reservation_time")
    private Instant reservationTime;

    @Column(name = "d_user_id")
    private Integer userId;


    @Column(name = "qty_baby")
    private Integer qtyBaby;

    @Column(name = "qty_adult")
    private Integer qtyAdult;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}