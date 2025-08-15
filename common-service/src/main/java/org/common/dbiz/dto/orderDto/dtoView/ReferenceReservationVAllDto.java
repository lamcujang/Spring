package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReferenceReservationVAllDto implements Serializable {
    private String nameReference;

    @Size(max = 64)
    String name;
    @Size(max = 15)
    String value;
}