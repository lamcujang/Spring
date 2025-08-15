package com.dbiz.app.proxyclient.business.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.proxyclient.domain}
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
    Integer tenantId;
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

}