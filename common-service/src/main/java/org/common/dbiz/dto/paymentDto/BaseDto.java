package org.common.dbiz.dto.paymentDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaseDto {

    Integer id;
    Integer tenantId;
    Integer orgId;
    String isActive;
    Integer createdBy;
    Integer updatedBy;
}
