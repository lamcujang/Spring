package org.common.dbiz.dto.paymentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.orderDto.PurchaseOrderLineDto;
import org.common.dbiz.dto.orderDto.dtoView.PODetailVDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class PurchaseInvoiceLineDto extends BaseDto  implements Serializable {
    Integer purchaseOrderId;
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
    BigDecimal discountPercent;
    BigDecimal discountAmount;
    BigDecimal netAmount;
    BigDecimal taxAmount;
    List<PODetailVDto> lines;
}