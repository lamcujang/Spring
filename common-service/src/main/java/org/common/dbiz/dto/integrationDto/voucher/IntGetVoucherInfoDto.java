package org.common.dbiz.dto.integrationDto.voucher;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IntGetVoucherInfoDto {

    private String voucherNo;
}
