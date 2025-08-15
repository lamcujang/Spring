package org.common.dbiz.dto.orderDto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReferenceDto {
    private BigDecimal referenceId;


    private String nameReference;

    private String name;

    private String value;
}


