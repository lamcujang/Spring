package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * DTO cho bảng pos.d_inventory_category_special_tax
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InventoryCategorySpecialTaxDto implements Serializable {
    Integer id;
    String code;
    String name;
    BigDecimal taxRate;
    String unit;
    Long subsectionCode;
    String subsectionName;
    String isParent;
    Integer parentId;
    Integer grade;
    String isActive;
}