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
public class TenantByUrlDto implements Serializable {
    Integer id;
    String code;
    String name;
    String domainUrl;
    String taxCode;
    Integer imageId;
    String industryCode;
    IndustryDto industry;
    UserDto user;

}