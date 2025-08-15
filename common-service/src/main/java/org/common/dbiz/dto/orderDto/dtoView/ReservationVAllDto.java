package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationVAllDto implements Serializable {
    BigDecimal id;
    @Size(max = 15)
    String phone;
    BigDecimal totalCus;
    @Size(max = 255)
    String company;
    @Size(max = 255)
    String customerName;
    String reservationTime;
    BigDecimal reserAmount;
    @Size(max = 10)
    String status;
    @Size(max = 20)
    String timeTocome;
    BigDecimal qtyAdult;
    BigDecimal qtyBaby;
    TableVAllDto table;
    FloorVDto floor;
    CustomerReservationVAllDto customer;
    UserReservationVAllDto user;
    ReferenceReservationVAllDto reservationStatus;
}