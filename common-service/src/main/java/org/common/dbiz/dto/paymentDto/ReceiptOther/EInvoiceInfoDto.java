package org.common.dbiz.dto.paymentDto.ReceiptOther;

import lombok.*;
import org.common.dbiz.dto.paymentDto.PaymentDetailDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceInfoDto {

    String invoiceForm;
    String invoiceSign;
    String invoiceNo;
    String searchCode;
    String searchLink;
    String supplierTaxCode;
    String taxOfCode;
}
