package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain.Floor}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FloorDto implements Serializable {
    String isActive;
    Integer id;
    @NotNull
    @Size(max = 5)
    String floorNo;
    @Size(max = 255)
    String name;
    @Size(max = 255)
    String description;

    @NotNull(message = "Org id cannot be null")
     Integer orgId;

    Integer qtyTables;
    Integer displayIndex;
    private Integer posTerminalId;
    Integer erpFloorId;


}