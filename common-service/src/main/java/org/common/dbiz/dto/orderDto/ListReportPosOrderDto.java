package org.common.dbiz.dto.orderDto;


import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ListReportPosOrderDto {

    ReportPosOrderDto sale;
    ReportPosOrderDto payment;
    ReportPosOrderDto refund;
    ReportPosOrderDto purchase;
    List<ShiftTaxDto> shiftTaxDtoList;
    Integer qtyTrans;
    Integer qtyTransSale;
    Integer qtyTransVoid;
    Integer qtyTransReturn;
    String startDate;
    String endDate;
    BigDecimal startCash;
    BigDecimal saleCash;
    BigDecimal endCash;

}
