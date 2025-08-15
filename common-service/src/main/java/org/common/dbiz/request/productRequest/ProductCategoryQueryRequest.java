package org.common.dbiz.request.productRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryQueryRequest extends BaseQueryRequest {
    private String code;
    private String name;
    private Integer orgId;
    private String isMenu;
    private String isCommodities;
    private String isGroup;
    private Integer productCategoryParentId;
    private String isSummary;
    private String isActive;
    private Integer posTerminalId;
    private Integer productCategoryId;
    private Integer userId;
    private String searchKey;
    private List<Integer> notInIds;
}