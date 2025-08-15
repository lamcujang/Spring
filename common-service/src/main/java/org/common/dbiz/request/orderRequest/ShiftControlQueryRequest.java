package org.common.dbiz.request.orderRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftControlQueryRequest extends BaseQueryRequest  implements Serializable {

    private String closeDate;
    private String shiftType;
    private Integer orgId;
    private Integer id;
    private Integer posTerminalId;
    private String fromDate;
    private String toDate;

}