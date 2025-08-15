package org.common.dbiz.request.paymentRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.request.BaseQueryRequest;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentQueryRequest extends BaseQueryRequest  implements Serializable {

    private Integer id;
    private Integer orgId;
    private Integer customerId;
    private Integer vendorId;
    private Integer invoiceId;
    private Integer bankAccountId;
    private String paymentDate;
    private String paymentStatus;
    private BigDecimal paymentAmount;
    private Integer orderId;

}
