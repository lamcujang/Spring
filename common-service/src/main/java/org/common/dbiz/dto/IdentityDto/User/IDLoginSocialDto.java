package org.common.dbiz.dto.IdentityDto.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDLoginSocialDto {

    List<String> schemas;
    List<String> attributes;
    String filter;
    String domain;
    Integer startIndex;
    Integer count;
}
