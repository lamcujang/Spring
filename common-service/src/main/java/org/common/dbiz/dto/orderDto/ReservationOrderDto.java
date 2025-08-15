package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.orderDto.response.UserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationOrderDto implements Serializable {
    Integer orgId;
    Integer id;
    @Size(max = 255)
    String name;

    @Size(max = 255)
    String customerName;
    @Size(max = 15)
    String phone;
    @Size(max = 255)
    String company;
    BigDecimal totalCus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String reservationTime;
    BigDecimal reserAmount;
    String status;
    String code;
    String timeTocome;

    Integer floorId;
    Integer tableId;
    Integer customerId;
    Integer userId;
    FloorDto floor;
    TableDto table;
    CustomerDto customer;
    UserDto user;

    ReservationStatusDto reservationStatus;
}