package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.dto.orderDto.response.KitchenOrderlineDtoV;
import org.common.dbiz.dto.productDto.JsonView.JsonViewKitchenOrder;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.view.KitchenOrderGetAllV}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderGetAllVDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    @Size(max = 32)
    String documentno;
    Integer posOrderId;
    Integer doctypeId;
    Integer warehouseId;
    @Size(max = 5)
    String orderStatus;
    @Size(max = 255)
    String description;
    Integer posTerminalId;
    Integer erpKitchenOrderId;
    @Size(max = 1)
    String isSyncErp;
    String dateordered;
    @Size(max = 64)
    String orderStatusName;
    FloorKcVDto floor;
    TableKcVDto table;
    UserKcVDto user;
    String time;
    String orgName;
    String orgCode;
    String posDocumentNo;
    String warehouseName;
    @JsonView(JsonViewKitchenOrder.viewJsonFindAll.class)
    List<KitchenOrderLineViewDto> listKitchenOrderline;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    List<KitchenOrderLineByStatusVDto> listKitchenOrderlineV;
}