package org.common.dbiz.dto.systemDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GetUrlEMenuDto implements Serializable {

    
    String eMenuUrl;
    String orgName;
    String FAndTName;
}