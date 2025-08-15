package org.common.dbiz.dto.integrationDto.voucher.checkin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckInVoucherDto {

    Integer customerId;
    Integer erpCustomerId;
    Integer erpOrderId;
    Integer erpPosTerminalId;
    Integer erpWarehouseId;
    Integer userId;
    Integer erpUserId;
    Integer orgId;
    String phoneNumber;
    String registerNo;
    List<CheckInVoucherDetailDto>  data;
}
