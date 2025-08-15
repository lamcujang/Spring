package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.PartnerGroup}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PartnerGroupDto implements Serializable {
    String isActive;
    Integer id;
    @NotNull
    Integer tenantId;
    @NotNull
    Integer orgId;
    @Size(max = 32)
    String groupCode;
    @Size(max = 128)
    String groupName;
    @Size(max = 255)
    String description;
    String isCustomer;
    String isDefault;
    BigDecimal discount;
    private Integer erpBpGroupId;
    private String erpBpGroupName;
    String company;
    PartnerGroupDto parent;


//    PartnerGroupDto partnerGoupParent;

}