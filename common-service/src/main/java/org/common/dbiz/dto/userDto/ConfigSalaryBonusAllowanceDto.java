package org.common.dbiz.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ConfigSalaryBonusAllowanceDto {
    Integer id;
    Integer salaryConfigId;
    Integer bonusId;
    Integer allowanceId;
}
