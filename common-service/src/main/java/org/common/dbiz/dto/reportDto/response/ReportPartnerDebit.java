package org.common.dbiz.dto.reportDto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.reportDto.response.customer.PartnerDebitDto;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportPartnerDebit {
    Integer tenantId;
    String name;
    String address;
    BigDecimal negOpening;
    BigDecimal posOpening;
    BigDecimal negClosing;
    BigDecimal posClosing;
    BigDecimal receivable;
    BigDecimal collected;
    List<PartnerDebitDto> partnerDebit;
}
