package org.common.dbiz.dto.paymentDto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.dbiz.app.paymentservice.domain.VoucherTransaction}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VoucherTransactionDto implements Serializable {

    Integer id;
    Integer orgId;
    Integer customerId;
    Integer vendorId;
    Integer bankAccountId;
    String transactionDate;
    BigDecimal amount;
    String voucherCode;
    Integer posOrderId;
    String ownerName;
}