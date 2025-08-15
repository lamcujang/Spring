package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * DTO for {@link com.dbiz.app.tenantservice.domain}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDto implements Serializable {
    Integer id;
    @Size(max = 32)
    String code;
    @NotNull
    @Size(max = 255)
    String name;
    @Size(max = 512)
    String address;
    @Size(max = 64)
    String email;
    @Size(max = 100)
    String area;
    @Size(max = 100)
    String wards;
    Integer erpOrgId;
    private String description;
    private String isSummary ;
    private String phone;
    private String taxCode;
    private String isActive = "Y";


}