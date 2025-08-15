package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableVAllDto implements Serializable {
    Integer id;
    @Size(max = 32)
    String name;
    @Size(max = 5)
    String tableNo;
    Integer displayIndex;
    @Size(max = 3)
    String tableStatus;
}