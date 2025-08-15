package org.common.dbiz.dto.reportDto;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.common.dbiz.dto.reportDto.respone.ExpenseTypeDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationExpenseDetailDto implements Serializable {
    private Integer id;
    private Integer taxDeclarationIndividualId;
    private Integer expenseTypeId;
    private BigDecimal expenseAmount;
    private String isActive;
    private ExpenseTypeDto expenseTypeDto;
}