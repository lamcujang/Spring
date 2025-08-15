package org.common.dbiz.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IndustryInitDto implements Serializable {
    Integer id;
    String code;
    String name;
    String groupType;
    String groupTypeName;
    String businesModal;
    String isActive;
}