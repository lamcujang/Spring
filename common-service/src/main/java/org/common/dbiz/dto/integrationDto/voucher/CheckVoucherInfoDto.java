package org.common.dbiz.dto.integrationDto.voucher;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckVoucherInfoDto {
    private Integer serviceId;
    private String voucherCode;
    private Integer erpUserId;
    private Integer userId;
    private Integer orgId;
}
