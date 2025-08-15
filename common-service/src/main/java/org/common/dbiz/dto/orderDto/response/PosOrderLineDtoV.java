package org.common.dbiz.dto.orderDto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.PosOrderline}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosOrderLineDtoV implements Serializable {
    @NotNull(message = "Org id cannot be null")
    Integer orgId;
    String isActive;
    Integer id;
    @NotNull
    Integer qty;
    @NotNull
    Integer productId;
    @Size(max = 255)
    String description;

     Integer posOrderId;
     String status;
     BigDecimal salesPrice;

}