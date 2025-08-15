package org.common.dbiz.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BaseDto {

    Integer id;
    Integer tenantId;
    Integer orgId;
    String isActive;
    Integer createdBy;
    Integer updatedBy;
}
