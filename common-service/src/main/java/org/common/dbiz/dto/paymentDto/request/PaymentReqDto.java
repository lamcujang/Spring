package org.common.dbiz.dto.paymentDto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReqDto {


    private Integer orgId;
    private String documentNo;
    private String dateFrom;
    private String dateTo;
    private Integer customerId;
    private Integer vendorId;
    private Integer userOtherId;
    private String docStatus;
    private String paymentMethod;
    private String docType;
    private String userSearchKey;
    private int page=0;
    private int pageSize=15;

}
