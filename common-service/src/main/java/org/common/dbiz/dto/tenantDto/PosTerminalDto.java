package org.common.dbiz.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;
import org.common.dbiz.dto.productDto.PriceListIntDto;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder

public class PosTerminalDto  implements Serializable {

    String name;
    String description;
    Integer userId;
    Integer bankAccountCashId;
    Integer docTypeId;
    String isRestaurant;
    Integer priceListId;
    Integer warehouseId;
    Integer bankAccountId;
    String printerIp;
    BigDecimal printerPort;
    String isBillMerge;
    String isNotifyBill;
    Integer bankAccountVisaId;
    Integer erpPosId;
    Integer id;
    Integer tenantId;
    Integer orgId;
    String isActive;
    Integer createdBy;
    Integer updatedBy;
    private String deviceToken;
    String printerType;
    String printerName;
    String printerProductId;
    String printerVendorId;
    Integer printerPageSize;
    Integer printerPageSoQty;
    Integer printerPageTempQty;
    String orgName;
    String priceListName;
    String erpPtmName;
    String warehouseName;
    String salesRepName;
    PriceListIntDto priceListIntDto;

}