package org.common.dbiz.dto.IdentityDto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class IDPhoneListDto {

    String type;
    String value;
}
