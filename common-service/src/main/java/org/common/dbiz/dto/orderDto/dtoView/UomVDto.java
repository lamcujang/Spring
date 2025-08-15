package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.view.UomV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UomVDto implements Serializable {
    Integer id;
    @Size(max = 15)
    String name;
}