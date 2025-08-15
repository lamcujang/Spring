package org.common.dbiz.dto.tenantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.userDto.UserDto;

import java.io.Serializable;

/**
 * DTO for
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetAssetsImageDto implements Serializable {
    String industryCode;
    String tenantCode;
    String type;// Banner, Background, Link

}