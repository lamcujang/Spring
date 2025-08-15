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
public class LotDto {

    Integer id;
    Integer tenantId;
    String code;
    Integer orgId;
    String orgName;
    ProductDto product;
    WarehouseDto warehouse;
    LocatorDto locator;
    VendorDto vendor;
    String expirationDate;
    String manufactureDate;
    String description;
    String lotStatus;
    String isActive;
    BigDecimal costPrice;
    BigDecimal onHandQty;
    BigDecimal saleQty;
    String created;
    String updated;

}
