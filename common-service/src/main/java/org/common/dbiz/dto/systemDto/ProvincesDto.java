package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvincesDto {
    String name;
    String codeName;
    String codeProvinces;
    String codeDistricts;
    String codeWards;
}
