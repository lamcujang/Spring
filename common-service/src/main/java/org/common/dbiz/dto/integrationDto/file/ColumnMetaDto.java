package org.common.dbiz.dto.integrationDto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ColumnMetaDto {

    private String columnName;

    private String dataType;
    private String isRequired;

    private Integer charMaxLength;   // for varchar
    private Integer numericPrecision; // for numeric(p,s)
    private Integer numericScale;     // for numeric(p,s)

}
