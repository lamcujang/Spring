package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListQueryRequest extends BaseQueryRequest implements Serializable {

        private String name;
        private String isActive;
        private String isSaleprice;
        private String fromDate;
        private String toDate;
        private Integer orgId;
        private String generalPriceList;
        private Integer[] orgIds;
        private String isAll;
        private String validDate;
        private Integer posTerminalId;
        private String currentDate;
        private Integer userId;
        private Integer id;
        private Integer roleId;

}
