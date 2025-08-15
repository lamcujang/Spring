package org.common.dbiz.request.tenantRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardCredential {
    String org;
    Integer orgId;
    String queryType;

}
