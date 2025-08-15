package org.common.dbiz.dto.reportDto.response.expenseList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
    public class ToTalExpense {
        private Long totalAmount;
        private String name;
        private Integer expenseTypeId;
    }
