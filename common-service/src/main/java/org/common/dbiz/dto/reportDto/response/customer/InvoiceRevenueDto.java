package org.common.dbiz.dto.reportDto.response.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceRevenueDto {
    Integer invoiceId;
    String documentNo;
    String dateInvoiced;
    String description;
    BigDecimal vatDist;
    BigDecimal pitDist;
    BigDecimal vatServ;
    BigDecimal pitServ;
    BigDecimal vatProd;
    BigDecimal pitProd;
    BigDecimal vatOthr;
    BigDecimal pitOthr;
}
