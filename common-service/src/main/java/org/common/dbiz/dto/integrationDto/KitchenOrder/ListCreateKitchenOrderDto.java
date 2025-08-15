package org.common.dbiz.dto.integrationDto.KitchenOrder;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ListCreateKitchenOrderDto {

    List<CreateKitchenOrderDto> data;
}
