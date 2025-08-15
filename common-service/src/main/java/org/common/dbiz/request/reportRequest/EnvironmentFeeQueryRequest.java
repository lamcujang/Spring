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
public class EnvironmentFeeQueryRequest extends BaseQueryRequest {
    private Integer id;
    private String itemCode;
    private String itemName;
    private Integer taxType;
    private String isActive;
}
