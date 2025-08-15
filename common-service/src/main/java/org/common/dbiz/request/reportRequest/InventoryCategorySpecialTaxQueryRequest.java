package org.common.dbiz.request.reportRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCategorySpecialTaxQueryRequest extends BaseQueryRequest {
    private Integer id;
    private String code;
    private String name;
    private String subsectionCode;
    private String subsectionName;
    private String isParent;
    private String isActive;
    private Integer parentId;
    private Integer grade;

}
