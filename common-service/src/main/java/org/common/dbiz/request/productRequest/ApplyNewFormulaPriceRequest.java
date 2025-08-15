package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyNewFormulaPriceRequest {
     private Integer productId;

     private BigDecimal salesPrice; // gia moi

    private String calculation; // cong thuc

    private BigDecimal operandB ; // toanHang

    private String operandA; // toanHang

     private Integer priceListId;

     private Boolean isAll;

    private String percent; // phan tram
}
