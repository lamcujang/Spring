package org.common.dbiz.dto.tenantDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.tenantservice.domain.SummaryTodayV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SummaryTodayVDto implements Serializable {
    Integer orgId;
    BigDecimal totalAmountToday;
    BigDecimal totalAmountYesterday;
    BigDecimal amountPercentDiff;
    Integer countOrderProcessing;
    BigDecimal amountOrderProcessing;
    Long countCustomerToday;
    Long countCustomerYesterday;
    BigDecimal customerPercentDiff;

    BigDecimal totalOrderToday;
    BigDecimal totalOrderYesterday;
    BigDecimal orderPercentDiff;

    BigDecimal otherAmtToday;
    BigDecimal otherAmtYesterday;
    BigDecimal otherAmtPercentDiff;

    BigDecimal discountAmtToday;
    BigDecimal discountAmtYesterday;
    BigDecimal discountAmtPercentDiff;

    Long qtyCancelToday;
    Long qtyCancelYesterday;
    BigDecimal qtyCancelPercentDiff;

}