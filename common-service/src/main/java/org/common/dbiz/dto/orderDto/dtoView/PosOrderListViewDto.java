package org.common.dbiz.dto.orderDto.dtoView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import org.common.dbiz.dto.orderDto.PosOrderLineVAllDto;
import java.util.List;

/**
 * DTO for {@link com.dbiz.app.domain.view.PosOrderListView}
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PosOrderListViewDto implements Serializable {
    String isActive;
    Integer orgId;
    Integer posOrderId;
    String documentNo;
    String orderDate;
    BigDecimal totalAmount;
    String orderStatus;
    Integer orderGuests;
    String orderStatusName;
    String customerName;
    BigDecimal customerId;
    BigDecimal paid;
    Integer userId;
    String userName;
    String fullName;
    Integer priceListId;
    String priceListName;
    String orgName;
    String description;
    String customerPhone;
    BigDecimal discountAmount;
    BigDecimal receiptOtherAmount;
    BigDecimal taxAmount;
    Integer shiftControlId;
    String priceCateCode;

    List<PosOrderLineCompleteVAllDto> posOrderLines;
}