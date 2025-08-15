package org.common.dbiz.dto.productDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductCDto implements Serializable {
    Integer id;
    String code;
    String name;
    BigDecimal saleprice;
    BigDecimal costprice;
    Integer taxId;
    Integer uomId;
    String uomCode;
    String uomName;
    String taxName;
    BigDecimal taxRate;
    BigDecimal qty;
}