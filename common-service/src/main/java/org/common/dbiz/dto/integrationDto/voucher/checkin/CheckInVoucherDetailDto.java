package org.common.dbiz.dto.integrationDto.voucher.checkin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckInVoucherDetailDto {

    Integer erpOrderLineId;
    String voucherCode;
    String statusCode;
    Integer voucherId;
    Integer serviceId;
    Integer productServiceId;

}
