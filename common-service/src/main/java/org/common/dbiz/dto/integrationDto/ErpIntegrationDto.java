package org.common.dbiz.dto.integrationDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpIntegrationDto implements Serializable {
    Integer id;
    @Size(max = 36)
    String erpPlatform;
    @Size(max = 36)
    String erpUrl;
    Integer adClientId;
    Integer adOrgId;
    Integer adRoleId;
    Integer warehouseId;
    @Size(max = 36)
    String username;
    @Size(max = 36)
    String password;
    @Size(max = 36)
    String description;
    @Size(max = 36)
    String isDefault;
    @Size(max = 36)
    String bankAccountType;
    Integer orgId;

}