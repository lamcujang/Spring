package org.common.dbiz.dto.userDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenaltyDeductionDto {
    private Integer id;
    private Integer orgId;
    private Integer tenantId;
    private Integer salaryConfigId;
    private String name;
    private String code;
    private String description;
    private BigDecimal penaltyAmount;
    private Integer warningCount;
    private String value;
    private String nameValue;

}
