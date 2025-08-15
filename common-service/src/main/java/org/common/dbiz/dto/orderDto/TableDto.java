package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.common.dbiz.dto.productDto.JsonView.JsonViewTable;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableDto implements Serializable {
    String isActive;
    Integer id;
    String tableNo;
    @NotNull
    @Size(max = 32)
    String name;
    @Size(max = 255)
    String description;

    @JsonView({JsonViewTable.viewFindAll.class, JsonViewTable.viewSaveAndUpdate.class , JsonViewTable.viewJsonTableAndReservation.class})
    FloorDto floor;

    String tableStatus;

     Integer displayIndex;

    Integer numberSeats;

    @NotNull(message = "Org id cannot be null")
    Integer orgId;

    @JsonView(JsonViewTable.viewJsonTableAndReservation.class)
    ReservationOrderDto reservationOrder;

    @JsonView(JsonViewTable.viewJsonTableAndReservation.class)
    List<Integer> posOrderIds;

    @JsonView(JsonViewTable.viewJsonTableAndReservation.class)
    ReservationTablePosVDto reservation;

    Integer posOrderId;
    private String isBuffet;
    private String isRoom;
    private BigDecimal numberGuests;
    private Integer erpTableId;
    private String isLocked;
    private String customerName;
    private String orderGuest;
    private String isDefault;
}