package org.common.dbiz.dto.reportDto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportReqDto {

    String orgOrAll;
    String empOrAll;
    String cusOrAll;
    String warehouseOrAll;
    String productOrAll;
    String productCategoryOrAll;
    String terminalOrAll;
    String productionOrAll;
    String statusOrAll;
    String shiftTypeOrAll;
    String shiftControlOrAll;
    String payMethodOrAll;
    Integer terminalId;
    Integer cusId;
    Integer empId;
    Integer orgId;
    Integer warehouseId;
    Integer productId;
    Integer productCategoryId;
    Integer productionId;
    Integer shiftControlId;
    String statusValue;
    String shiftTypeValue;
    String payMethodValue;
    String queryType;
    String startDate;
    String endDate;
    String partnerHasData;
    String showInventory;
    List<Integer> orgIds;
    List<Integer> partnerIds;
    List<Integer> productIds;
}
