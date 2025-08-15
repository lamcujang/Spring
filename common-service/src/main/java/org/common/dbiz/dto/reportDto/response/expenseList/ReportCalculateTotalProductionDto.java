package org.common.dbiz.dto.reportDto.response.expenseList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.paymentDto.PaymentDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReportCalculateTotalProductionDto {
    Integer tenantId;
    String name;
    String address;
    List<PaymentDto> payment;
    List<ToTalExpense> toTalExpense;
}
