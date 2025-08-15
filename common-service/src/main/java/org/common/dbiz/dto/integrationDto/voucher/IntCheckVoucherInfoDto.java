package org.common.dbiz.dto.integrationDto.voucher;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IntCheckVoucherInfoDto implements Serializable {

    private Integer ad_Client_ID;
    private Integer serviceId;
    private String voucherCode;
    private Integer ad_User_ID;

}
