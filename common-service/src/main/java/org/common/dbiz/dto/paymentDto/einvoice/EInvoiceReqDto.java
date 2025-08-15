package org.common.dbiz.dto.paymentDto.einvoice;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EInvoiceReqDto {

    GeneralInvoiceInfoDto generalInvoiceInfo;
    BuyerInfoDto buyerInfo;
    List<PaymentDto> payments;
    List<ItemInfoDto> itemInfo;
    SellerInfoDto sellerInfo;
//    MetadataDto metadata;
}
