package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.paymentservice.domain.PaymentType}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentTypeDto implements Serializable {
    String isActive;
    Integer id;
    Integer orgId;
    @Size(max = 3)
    String code;
    @Size(max = 128)
    String paymentName;
    @Size(max = 3)
    String paymentType;
    @Size(max = 512)
    String imageUrl;
    @Size(max = 1)
    String isEnable;
    BigDecimal sequenceNo;
    @Size(max = 15)
    String imageCode;
    String paymentTypeCode;
    String paymentTypeName;
}