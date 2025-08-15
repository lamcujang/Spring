package org.common.dbiz.dto.orderDto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PromotionTime}
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PromotionTimeDto implements Serializable {
    String isActive;
    Integer id;
    Integer promotionId;
    String fromHour;
    String toHour;
}