package org.common.dbiz.dto.tenantDto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.productDto.PricelistDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.userDto.UserDto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.tenantservice.domain.view.PosTerminalV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PosTerminalVDto extends BaseDto implements Serializable {

    String name;
    String description;
    String isRestaurant;
    String printerIp;
    Integer printerPort;
    String isBillMerge;
    String isNotifyBill;
    Integer erpPosId;
    UserDto user;
    WarehouseDto warehouse;
    PricelistDto priceList;
    BankAccountDto bankAccount;
    BankAccountDto bankAccountCash;
    BankAccountDto bankAccountVisa;

    String printerType;
    String printerName;
    String printerProductId;
    String printerVendorId;
    Integer printerPageSize;
    Integer printerPageSoQty;
    Integer printerPageTempQty;
}