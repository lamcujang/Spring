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
public class IntCheckInVoucherDto {

    Integer ad_Client_ID;
    Integer c_BPartner_ID;
    Integer c_Order_ID;
    Integer ad_User_ID;
    String phoneNumber;
    String registerNo;
    Integer m_Warehouse_ID;
    Integer terminalID;
    List<IntCheckInVoucherDetailDto>  data;
}
