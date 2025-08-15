package org.common.dbiz.request.paymentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountQueryRequest extends BaseQueryRequest implements Serializable {
    private String isDefault;
    private String keyword;
    private Integer bankId;
    private Integer id;
    private String notCash;
    private String isCash;
    private String isActive;
    private String swiftCode;
    private Integer orgId;
    private Integer posTerminalId;
}
