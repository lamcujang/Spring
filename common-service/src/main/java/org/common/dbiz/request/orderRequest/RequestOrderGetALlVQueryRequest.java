package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderGetALlVQueryRequest extends BaseQueryRequest  implements Serializable {
   String keyword;
   Integer floodId;
   Integer tableId;
   String  orderTime;
   String status;
   Integer orgId;
   String fromDate;
    String toDate;
}
