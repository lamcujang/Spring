package com.dbiz.app.orderservice.domain.view;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Embeddable
@Getter
public class ReservationTablePosV implements Serializable {
    @Size(max = 20)
    @Column(name = "time_tocome", length = 20)
    private String timeTocome;

    @Column(name = "reservation_time")
    private Instant reservationTime;

    @Column(name = "d_reservation_order_id")
    private Integer reservationOrderId;
}