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
public class WardDto {

    Integer id;
    String code;
    String name;
    Integer provinceId;

    String hasMerger;
    String mergerDetails;
//    String administrativeCenter;

//    String provinceShortName;
//    String provinceShortCode;
//    String provincePlaceType;
//    Boolean provinceIsMerged;
//    String provinceMergedWith;

}
