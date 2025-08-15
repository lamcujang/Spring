package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.selimhorri.app.domain.view.ShiftControlGetPaymentV}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftControlGetPaymentVDto implements Serializable {

    Integer paymentId;
    String isActive;
    Integer orgId;
    Integer posOrderId;
    @Size(max = 32)
    String documentNo;
    Integer shiftControlId;
    String orderDate;
    @Size(max = 255)
    String customerName;
    @Size(max = 128)
    String userName;
    @Size(max = 3)
    String paymentMethod;
    BigDecimal totalPayment;
    String paymentType;
}