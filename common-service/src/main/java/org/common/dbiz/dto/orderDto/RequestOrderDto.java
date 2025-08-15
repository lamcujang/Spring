package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestOrderDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer doctypeId;
    @Size(max = 32)
    String documentNo;
    @Size(max = 5)
    String orderStatus;
    Integer floorId;
    Integer tableId;
    String orderTime;
    String cusPhone;
    String cusName;
    Integer posTerminalId;
    String isSendKitchen;
    List<RequestOrderLineDto> requestOrderLineDto;
}