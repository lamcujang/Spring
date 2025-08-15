package org.common.dbiz.dto.orderDto.dtoView;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VendorPOVDto implements Serializable {
    Integer vendorId;
    String vendorCode;
    String vendorName;
    String vendorPhone;
    BigDecimal vendorDebt;
    BigDecimal vendorPaid;
    String address1;
    String taxCode;
}