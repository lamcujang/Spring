package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO cho bảng pos.d_environment_fee
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxBusinessIndustryDto implements Serializable {
    
    Integer id;
    
    String industryCode;
    
    String industryName;

    String isActive;
}