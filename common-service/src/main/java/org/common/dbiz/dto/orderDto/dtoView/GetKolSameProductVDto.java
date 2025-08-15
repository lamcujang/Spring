package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.view.GetKolSameProductV}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetKolSameProductVDto implements Serializable {
    Integer id;
    Integer kitchenOrderId;
    Integer productId;
    Integer qty;
    @Size(max = 255)
    String description;
    @Size(max = 1)
    String isActive;
    Integer orgId;
    @Size(max = 255)
    String note;
    @Size(max = 64)
    String nameStatus;
    @Size(max = 5)
    String orderLineStatus;
    Integer tableId;
    @Size(max = 32)
    String tableName;
    @Size(max = 5)
    String tableNo;
    Integer floorId;
    @Size(max = 255)
    String floorName;
    @Size(max = 20)
    String floorNo;
    @Size(max = 255)
    String productName;
    @Size(max = 128)
    String posName;
    Integer waitingTime;
    Integer completedTime;
    String documentNo;
    String created;
    private String cancelReason;
    private Integer cancelReasonId;

}