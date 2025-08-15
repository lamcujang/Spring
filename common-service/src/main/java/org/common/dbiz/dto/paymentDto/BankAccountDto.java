package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.domain}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BankAccountDto implements Serializable {
    Integer id;
    Integer bankId;
    @Size(max = 32)
    String accountNo;
    @Size(max = 255)
    String description;
    @Size(max = 255)
    String name;
    @Size(max = 1)
    String isDefault;
    @Size(max = 3)
    String bankAccountType;
    Integer posTerminalId;
    Integer orgId;
    String isActive;

    String bankName;
    String bankCode;
    String swiftCode;
    BankDto bank;
    String imageUrlBank;
    String nameBank;
    String codeBank;
    private String branch;

}