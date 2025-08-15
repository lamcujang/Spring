package org.common.dbiz.dto.integrationDto.posOrder;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CreateOrderDto {

    private Integer ad_Client_ID;
    private Integer ad_Org_ID;
    private Integer m_PriceList_ID;
    private Integer ad_User_ID;
    private Integer terminalID;
    private Integer cus_Paymethod_ID;
    private String cusName;
    private String cusPhone1;
    private String cusPhone2;
    private String cusVipStatus;
    private BigDecimal cusDiscountPer;
    private BigDecimal cusDiscountAmt;
    private BigDecimal flatDiscount;
    private BigDecimal flatAmt;
    private String qrCode;
    private String orderRef;
    private String dateOrdered;
    private BigDecimal grandTotal;
    private String paymentType;
    private String imageBlob;
    private String buffet;
    private String type;
    private String bankName;
    private String bankNo;
    private String tableName;
    private String bankName1;
    private String tableName1;
    private Integer guest;
    private String tableNo;
    private String floorNo;
    private Integer D_Pos_Order_ID;
    private List<PaymentInfoDto> paymentsInfo;
    private List<LineInfoDto> linesInfo;
    private List<LineInfoDto> receiptOtherInfo;
    private List<LineInfoDto> deductionInfo;
    private ShiftInfoDto shiftInfo;


}
