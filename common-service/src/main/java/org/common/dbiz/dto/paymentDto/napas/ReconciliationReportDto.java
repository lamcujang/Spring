package org.common.dbiz.dto.paymentDto.napas;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReconciliationReportDto {

    String beginDate;
    String endDate;
    String receivingReport;
    String reportId;
    String creationDateTime;
    Integer pageNumber;
    Integer totalPage;
    List<ReconciliationReportDTDto> tranxDetail;
}
