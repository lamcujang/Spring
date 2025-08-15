package org.common.dbiz.dto.paymentDto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.paymentDto.BankDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.tenantDto.PosTerminalDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NpIntRequestResDto {

    String requestStatus;
    String requestStatusName;
    String phone;
    BankDto bankDto;
    BankAccountDto bankAccountDto;
    PosTerminalDto posTerminalDto;
    OrgDto orgDto;

}
