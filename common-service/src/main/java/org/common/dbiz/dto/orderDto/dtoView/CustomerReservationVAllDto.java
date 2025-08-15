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
public class CustomerReservationVAllDto implements Serializable {
    Integer id;
    @Size(max = 64)
    String email;
    @Size(max = 255)
    String address;
    @Size(max = 255)
    String name;
}