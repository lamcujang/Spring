package org.common.dbiz.dto.reportDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseholdIndustryDto implements Serializable {
    Integer id;
    Integer taxHouseholdProfileId;
    Integer taxBusinessIndustryId;
    String isActive;
    TaxBusinessIndustryDto taxBusinessIndustryDto;
}