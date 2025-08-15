package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportPosQueryRequest extends BaseQueryRequest  implements Serializable {
        String reportType;// BY_DATE, BY_SHIFT
        String reportDateFrom;
        String reportDateTo;
        String currentDate;
        String shiftType;
        String documentNo;
        String name;
        String cashier;


}
