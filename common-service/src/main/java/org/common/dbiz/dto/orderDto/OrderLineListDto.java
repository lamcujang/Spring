package org.common.dbiz.dto.orderDto;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class OrderLineListDto extends BaseDto  implements Serializable {

    List<OrderLineDto> lines;
}
