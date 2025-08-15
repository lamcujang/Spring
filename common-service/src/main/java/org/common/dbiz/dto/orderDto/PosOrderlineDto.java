package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.common.dbiz.dto.orderDto.response.ProductDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties({"posOrderLines"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PosOrderlineDto implements Serializable {
    @NotNull(message = "Org id cannot be null")
    Integer orgId;
    String isActive;
    Integer id;
    @NotNull
    BigDecimal qty;
    @NotNull
    Integer productId;
    @Size(max = 255)
    String description;
    Integer taxId;
    Integer posOrderId;
    String status;
    BigDecimal salesPrice;
    BigDecimal lineNetAmt;
    BigDecimal grandTotal;
    BigDecimal taxAmount;
    BigDecimal discountPercent;
    BigDecimal discountAmount;
    Integer cancelReasonId;
    String cancelReasonMessage;
    ProductDto productDto;
    Integer kitchenOrderLineId;
    List<PosOrderLineDetailDto> lineDetails;
}