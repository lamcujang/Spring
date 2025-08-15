package org.common.dbiz.dto.paymentDto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.paymentDto.BankAccountDto;
import org.common.dbiz.dto.tenantDto.OrgDto;
import org.common.dbiz.dto.userDto.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRespDto {

    private Integer id;
    private String documentNo;
    private String paymentDate;
    private BigDecimal paymentAmount;
    private String description;
    private String referenceNo;
    private String userGroup;
    private String userGroupName;
    private String paymentMethod;
    private String paymentMethodName;
    private String docStatus;
    private String docStatusName;
    private String doctypeCode;
    private String docTypeName;

    private UserDto user;
    private OrgDto org;
//    private UserOtherDto userOther;
//    private CustomerDto customer;
//    private VendorDto vendor;
//    private UserDto employee;
    private BankAccountDto bankAccount;
    private VariousUserDto responsibleUser;
}
