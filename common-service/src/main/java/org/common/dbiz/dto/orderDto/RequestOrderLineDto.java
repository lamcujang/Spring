package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestOrderLineDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer productId;
    Integer requestOrderId;
    BigDecimal qty;
    String description;
    BigDecimal saleprice;
    BigDecimal totalAmount;
    Integer taxId;
    List<RqOrderLineDetailDto> lineDetail;
}