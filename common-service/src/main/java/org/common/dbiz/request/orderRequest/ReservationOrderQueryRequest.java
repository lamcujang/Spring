package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationOrderQueryRequest extends BaseQueryRequest  implements Serializable {
   Integer orgId;
    Integer floorId;
    Integer tableId;
    String[] status;
    Integer customerId;
    String reservationTime;
    String keyword;
    String customerName;
    String fromDate;
    String toDate;
    Integer id;
}
