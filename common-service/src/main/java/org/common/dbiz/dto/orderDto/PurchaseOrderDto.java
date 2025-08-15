package org.common.dbiz.dto.orderDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class PurchaseOrderDto extends BaseDto  implements Serializable {

    Integer doctypeId;
    Integer userId;
    Integer vendorId;
    String documentNo;
    String orderStatus;
    String orderDate;
    BigDecimal totalAmount;
    String description;
    Integer warehouseId;
    Integer locatorId;
    List<PurchaseOrderLineDto> purchaseOrderLines;
    Float discountPercent;
    BigDecimal discountAmount;
    BigDecimal netAmount;
    BigDecimal taxAmount;
    String paymentMethod;
    String paymentStatus;
    Integer bankAccountId;
    String paymentDate;
}