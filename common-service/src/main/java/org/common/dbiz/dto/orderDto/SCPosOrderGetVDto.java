package org.common.dbiz.dto.orderDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dbiz.app.domain.SCPosOrderGetV}
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SCPosOrderGetVDto implements Serializable {
    String isActive;
    Integer tenantId;
    Integer orgId;
    Integer shiftControlId;
    private BigDecimal Cash;
    private BigDecimal Bank;
    private BigDecimal Debt;
    private BigDecimal Loyalty;
    BigDecimal total;
    private Integer qtyBank;
    private Integer qtyCash;
    private Integer qtyDeb;
    private Integer qtyLoyalty;
    private BigDecimal visa;
    private Integer qtyVisa;
    private BigDecimal card;
    private Integer qtyCard;
    private BigDecimal cheque;
    private Integer qtyCheque;
    private BigDecimal free;
    private Integer qtyFree;
    private BigDecimal mix;
    private Integer qtyMix;
    private BigDecimal qrcode;
    private Integer qtyQrcode;
    private BigDecimal slip;
    private Integer qtySlip;
    private BigDecimal voucher;
    private Integer qtyVoucher;
    private BigDecimal coupon;
    private Integer qtyCoupon;
    private Integer totalQty;

}