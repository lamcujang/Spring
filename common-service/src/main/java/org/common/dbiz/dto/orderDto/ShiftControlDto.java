package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.ShiftControl}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftControlDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer id;
    Integer userId;
    Integer sequenceNo;
    Integer posTerminalId;
    String startDate;
    String endDate;
    @Size(max = 3)
    String shiftType;
    @Size(max = 1)
    String isClosed;
    Integer erpShiftControlId;
    BigDecimal startCash;
    BigDecimal transferCash;
    BigDecimal cashDiff;
    Integer doctypeId;
    @Size(max = 100)
    String documentNo;
    BigDecimal endCash;
    BigDecimal onCash;
    String shiftTypeName;
    private String descriptions;
    private Integer qtyTrans;
    private Integer qtyTransSale;
    private Integer qtyTransReturn;
  List<ShiftTaxDto> shiftTaxDtoList;



    SCPosOrderGetVDto posOrderRP;

    SCPosOrderGetVDto paymentOrderRP;

    SCPosOrderGetVDto purchaseOrderRP;

    SCPosOrderGetVDto returnGoodsRP;
}