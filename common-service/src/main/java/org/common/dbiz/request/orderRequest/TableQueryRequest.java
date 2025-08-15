package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableQueryRequest extends BaseQueryRequest  implements Serializable {

        private String tableNo;
        private String name;
        private String isActive;
        private String isDefault;
        private String tableStatus;
        private Integer orgId;
        private Integer floorId;
        private String reservationDate;
        private Integer tableId;
        private Integer posTerminalId;
}
