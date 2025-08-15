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
public class UserReservationVAllDto implements Serializable {
    Integer id;
    @Size(max = 64)
    String email;
    String name;
}