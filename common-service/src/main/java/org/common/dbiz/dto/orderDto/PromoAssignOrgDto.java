package org.common.dbiz.dto.orderDto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PromoAssignOrg}
 */
@AllArgsConstructor
@Getter
@Setter
@Builder@NoArgsConstructor
public class PromoAssignOrgDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    Integer promotionId;
    String nameOrg;
    String code;
}