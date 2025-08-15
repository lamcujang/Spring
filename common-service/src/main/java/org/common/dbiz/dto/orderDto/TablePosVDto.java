package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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
public class TablePosVDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer orgId;
    Integer id;
    @Size(max = 32)
    String name;
    @Size(max = 3)
    String tableStatus;
    BigDecimal displayIndex;
    Integer numberSeats;
    @Size(max = 5)
    String tableNo;
    Integer floorId;
    Integer posOrderId;
    @Size(max = 255)
    FloorDto floor;
    @Size(max = 255)
    String customerName;
    Integer customerId;
//    Integer userId;
//    ReservationTablePosVDto reservation;
    String isOccupied;

    private String isBuffet;
    private String isRoom;
    private BigDecimal numberGuests;
    private Integer erpTableId;
    private String isLocked;
    private String orderGuest;
    private String isDefault;
}