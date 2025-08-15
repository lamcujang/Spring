package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.view}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FloorVDto implements Serializable {
    Integer id;
    @Size(max = 255)
    String name;
    Integer displayIndex;
    String floorNo;
}