package org.common.dbiz.dto.reportDto.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.paymentDto.PaymentDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportCashBook {
    List<PaymentDto> payments;
    Integer tenantId;
    String name;
    String address;
    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal openingBalance;
    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal closingBalance;
    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal totalInAmount;
    @JsonSerialize(using = ToStringSerializer.class)
    BigDecimal totalOutAmount;
}
