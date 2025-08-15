package org.common.dbiz.dto.paymentDto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BankIntegrationInfoDto {


    String binCode;
    String merchantCode;
    String branchCode;
    String posCode;
    String industryCode;
}
