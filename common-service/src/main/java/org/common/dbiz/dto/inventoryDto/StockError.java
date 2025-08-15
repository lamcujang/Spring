package org.common.dbiz.dto.inventoryDto;


import lombok.*;
import lombok.experimental.SuperBuilder;


@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class StockError {

    protected String message;
}
