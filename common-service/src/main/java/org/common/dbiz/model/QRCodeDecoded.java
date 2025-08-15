package org.common.dbiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QRCodeDecoded {

    Integer tenantId;
    Integer posOrderId;
    Long timestamp;
}
