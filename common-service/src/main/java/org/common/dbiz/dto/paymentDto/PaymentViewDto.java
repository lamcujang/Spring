package org.common.dbiz.dto.paymentDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentViewDto implements Serializable {
    Integer id;
    @JsonProperty("d_invoice_id")
    Integer invoiceId;
    String documentNo;
    String paymentDate;
    String customerName;
    String vendorName;
    String paymentStatus;
    String paymentRule;
    BigDecimal paymentAmount;
    Integer purchaseInvoiceId;
    Integer userId;
    String fullName;
}