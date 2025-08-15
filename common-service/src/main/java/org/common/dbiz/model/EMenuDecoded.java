package org.common.dbiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EMenuDecoded {
   // int tenantId, int orgId, int tableId, int floorId, int terminalId, String tableNo, String floorNo, int priceListId
    Integer tenantId;
    Integer orgId;
    Integer tableId;
    Integer floorId;
    Integer terminalId;
    String tableNo;
    String floorNo;
    Integer priceListId;
    String orgName;
    String address;
}
