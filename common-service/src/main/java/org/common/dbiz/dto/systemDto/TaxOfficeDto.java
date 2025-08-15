package org.common.dbiz.dto.systemDto;

import lombok.*;
import lombok.Builder.Default;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaxOfficeDto {

    private Integer id;
    private String code;
    private String name;
    private Integer provinceId;
    private String oldName;
    private String isParent;
    private String parentCode;

    @Singular("child")
    private List<TaxOfficeDto> childDto;
}
