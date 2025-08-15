package org.common.dbiz.dto.inventoryDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.productDto.LocatorDto;
import org.common.dbiz.dto.productDto.ProductDto;
import org.common.dbiz.dto.productDto.WarehouseDto;
import org.common.dbiz.dto.userDto.VendorDto;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateLotDto {

    Integer id;
    String code;
    Integer orgId;
    String orgName;
    Integer productId;
    Integer warehouseId;
    Integer locatorId;
    Integer vendorId;
    String expirationDate;
    String manufactureDate;
    String description;
    String lotStatus;
    String isActive;
    BigDecimal costPrice;
    BigDecimal onHandQty;
    BigDecimal saleQty;


}
