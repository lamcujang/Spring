package org.common.dbiz.dto.productDto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListProductReqDto extends BaseQueryRequest {

    private Integer orgId;
    private Integer priceListId;
    private String barcode;
    private Integer posTerminalId;

    private Integer productId;
    private String name;
    private String productType;
    private Integer categoryId;

}
