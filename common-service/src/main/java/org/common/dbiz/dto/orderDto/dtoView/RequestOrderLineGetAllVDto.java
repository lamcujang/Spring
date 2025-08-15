package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.common.dbiz.dto.orderDto.RqOrderLineDetailDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.view.RequestOrderLineGetAllV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestOrderLineGetAllVDto implements Serializable {
    Integer orgId;
    Integer id;
    ProductVDto product;
    UomVDto uom;
    BigDecimal qty;
    String description;
    BigDecimal saleprice;
    BigDecimal totalAmount;
    RequestOrderGetAllVDto requestOrder;
    private String imageUrl;
    private Integer taxId;
    private String taxName;
    private BigDecimal taxRate;
    List<RqOrderLineDetailDto> lineDetail;

}