package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.view.TableKcV}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableKcVDto implements Serializable {
    Integer id;
    @Size(max = 5)
    String tableNo;
    @Size(max = 32)
    String nameTable;
}