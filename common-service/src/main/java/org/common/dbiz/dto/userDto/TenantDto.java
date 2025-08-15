package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link Tenant}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TenantDto implements Serializable {
    Integer id;
    String code;
    String name;
    String domainUrl;
    String taxCode;
    Integer imageId;

    Integer industryId;


}