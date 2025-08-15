package org.common.dbiz.request.intergrationRequest.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntPartnerCredential {
    private Integer clientId;
    private String intType;
    private String intDate;
}
