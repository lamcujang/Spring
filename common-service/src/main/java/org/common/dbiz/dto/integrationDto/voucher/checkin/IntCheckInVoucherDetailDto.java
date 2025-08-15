package org.common.dbiz.dto.integrationDto.voucher.checkin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IntCheckInVoucherDetailDto {

    Integer c_OrderLine_ID;
    String voucherCode;
    String statusCode;
    Integer voucher_id;
    Integer service_id;
    Integer dbiz_product_service_id;
}
