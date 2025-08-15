package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.view.RequestOrderGetAllV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestOrderGetAllVDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    CustomerVDto customer;
    String orderTime;
    TableVAllDto table;
    FloorVDto floor;
    @Size(max = 5)
    String orderStatus;
    private Integer priceListId;
    Integer posTerminalId;
    String statusName;
    private List<RequestOrderLineGetAllVDto> requestOrderLines;
}