package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.selimhorri.app.domain.view.GetListPosOrderV}
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetListPosOrderVDto implements Serializable {
    String isActive;
    Integer orgId;
    BigDecimal posOrderId;
    @Size(max = 32)
    String documentNo;
    Integer shiftControlId;
    String orderDate;
    @Size(max = 255)
    String name;
    @Size(max = 3)
    String paymentMethod;
    BigDecimal totalInvoice;
    BigDecimal totalPayment;
    BigDecimal dbeb;
    @Size(max = 5)
    String orderStatus;
}