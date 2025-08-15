package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO cho bảng pos.d_environment_fee
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnvironmentFeeDto implements Serializable {
    
    Integer id;
    
    String itemCode;
    
    String itemName;
    
    String unitName;
    
    BigDecimal taxRate;
    
    BigDecimal taxPrice;
    
    BigDecimal provincialIncomeTaxculationPrice;
    
    BigDecimal taxAblePrice;
    
    BigDecimal minFee;
    
    BigDecimal maxFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    String applyFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    String applyTo;
    
    String isActive;
    
    Integer taxType;

    String resourceType;
}