package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.common.dbiz.dto.BaseDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class POHeaderVDto extends BaseDto  implements Serializable {

    String orderStatus;
    BigDecimal totalAmount;
    String orderDate;
    String nameReference;
    BigDecimal totalProduct;
    BigDecimal totalQty;
    String documentNo;
    String description;
    BigDecimal discountPercent;
    BigDecimal discountAmount;
    BigDecimal netAmount;
    BigDecimal taxAmount;
    UserPOVDto userDto;
    VendorPOVDto vendorDto;
    WarehousePOVDto warehouseDto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<PODetailVDto> lines;
}