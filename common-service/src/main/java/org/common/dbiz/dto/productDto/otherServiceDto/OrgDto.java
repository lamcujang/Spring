package org.common.dbiz.dto.productDto.otherServiceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrgDto {
    Integer id;
    String name;
    private String area;
    private String isActive ;
    private String phone;


}
