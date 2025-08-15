package org.common.dbiz.dto.orderDto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.common.dbiz.dto.orderDto.KitchenOrderlineDto;
import org.common.dbiz.dto.orderDto.dtoView.KitchenOrderLineByStatusVDto;
import org.common.dbiz.dto.orderDto.dtoView.KitchenOrderLineViewDto;
import org.common.dbiz.dto.productDto.JsonView.JsonViewKitchenOrder;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KitchenOrderDtoV implements Serializable {


    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    String isActive;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer tenantId;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer orgId;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer id;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    String documentno;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer posOrderId;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer doctypeId;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    Integer warehouseId;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    String dateordered;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class
                ,JsonViewKitchenOrder.viewJsonKitchenOrderGroupProductLine.class})
    String floorName;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class
        ,JsonViewKitchenOrder.viewJsonKitchenOrderGroupProductLine.class})
    String tableName;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    String orderStatus;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    String description;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAll.class, JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    UserDto userDto;

    @JsonView(JsonViewKitchenOrder.viewJsonFindAll.class)
    List<KitchenOrderLineViewDto> listKitchenOrderline;

    @JsonView({JsonViewKitchenOrder.viewJsonFindAllByStatus.class})
    List<KitchenOrderLineByStatusVDto> listKitchenOrderlineV;


    // json reponse for kitchen order group product line
    @JsonView(JsonViewKitchenOrder.viewJsonKitchenOrderGroupProductLine.class)
    Integer qtyProduct;

    @JsonView(JsonViewKitchenOrder.viewJsonKitchenOrderGroupProductLine.class)
    Integer kitchenOrderLineId;

    String time;


}