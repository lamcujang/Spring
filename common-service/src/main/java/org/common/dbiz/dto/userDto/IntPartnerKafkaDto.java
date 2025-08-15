package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IntPartnerKafkaDto implements Serializable {
    Integer tenantId;
    String status = "COM";
    String error = "";
    List<CustomerDto> customers =new ArrayList<>();
    List<VendorDto> vendors =new ArrayList<>();
    List<PartnerGroupDto> partnerGroups =new ArrayList<>();
}