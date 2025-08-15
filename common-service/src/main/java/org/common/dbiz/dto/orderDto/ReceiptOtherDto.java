package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.orderservice.domain.ReceiptOther}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptOtherDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    @Size(max = 32)
    String code;
    @Size(max = 255)
    String name;
    BigDecimal amountOrPercent;
    @Size(max = 1)
    String isPercent;
    @Size(max = 1)
    String isAutoAllocate;
    @Size(max = 1)
    String isAutoReturn;

    String isCal;

    Integer[] orgIds;

    List<ReceiptOtherOrgDto> receiptOtherOrgs;
}