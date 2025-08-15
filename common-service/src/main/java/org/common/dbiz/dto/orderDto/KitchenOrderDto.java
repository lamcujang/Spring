package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonView;
import org.common.dbiz.dto.orderDto.response.KitchenOrderlineDtoV;
import org.common.dbiz.dto.orderDto.response.UserDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewKitchenOrder;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer orgId;
    Integer id;
    @Size(max = 32)
    String documentno;
    @NotNull
    Integer posOrderId;
    Integer doctypeId;
    Integer warehouseId;
    String dateordered;
    Integer userId;
    Integer floorId;
    Integer tableId;
    @Size(max = 5)
    String orderStatus;
    @Size(max = 255)
    String description;
    TableDto table;
    FloorDto floor;
    UserDto userDto;
    Integer posTerminalId;
    Integer erpKitchenOrderId;
    String isSyncErp;
    String nameFloor;
    String nameTable;
    Integer orderGuest;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class,JsonViewKitchenOrder.viewJsonFindAll.class,JsonViewKitchenOrder.viewJsonSaveUpdateEntity.class})
    List<KitchenOrderlineDto> listKitchenOrderline;

    @JsonView(JsonViewKitchenOrder.viewJsonFindAllByStatus.class)
    List<KitchenOrderlineDtoV> listKitchenOrderlineV;

    List<PrinterDto> printerDto;
}