package org.common.dbiz.dto.orderDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderMultiSourceNotiDto {

    Integer tenantId;
    Integer userId;
}
