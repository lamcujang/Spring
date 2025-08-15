package org.common.dbiz.dto.tenantDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
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
public class TempSalesSummaryDto implements Serializable {
    Integer orgId;
    Integer maxQty;
    JsonNode productObject;
    JsonNode sequenceNumberObject;

}