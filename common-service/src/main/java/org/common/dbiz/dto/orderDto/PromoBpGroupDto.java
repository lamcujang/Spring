package org.common.dbiz.dto.orderDto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.PromoBpGroup}
 */
@AllArgsConstructor
@Getter
@Setter
@Builder@NoArgsConstructor
public class PromoBpGroupDto implements Serializable {
    String isActive;
    Integer id;
    Integer promotionId;
    Integer bpGroupId;
    String namePartnerGroup;
    String codePartnerGroup;
}