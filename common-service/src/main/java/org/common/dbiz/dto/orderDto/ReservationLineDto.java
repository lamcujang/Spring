package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationLineDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer orgId;
    Integer id;
    @Size(max = 255)
    String description;
    @NotNull
    Integer reservationOrderId;
    @NotNull
    BigDecimal productId;
    @NotNull
    BigDecimal qty;
}