package org.common.dbiz.dto.systemDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProvinceDto {

    Integer id;
    String code;
    String name;
    String shortName;
    String shortCode;
    String placeType;

    String isMerged;
    String mergedWith;

    List<WardDto> wards;

}
