package org.common.dbiz.dto.paymentDto.ReceiptOther;

import lombok.*;
import org.common.dbiz.dto.orderDto.ReceiptOtherDto;
import org.common.dbiz.dto.productDto.TaxDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.paymentservice.domain.PosReceiptOther}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PosReceiptOtherDto implements Serializable {

    Integer id;
    Integer orgId;
    String receiptName;
    String code;
    String receiptWithTaxName;
    Integer taxId;
    Integer receiptOtherId;
    BigDecimal amountOrPercent;
    BigDecimal totalAmount;
    BigDecimal receiptAmount;
    BigDecimal taxAmount;
    BigDecimal receiptAmountWithTax;

}