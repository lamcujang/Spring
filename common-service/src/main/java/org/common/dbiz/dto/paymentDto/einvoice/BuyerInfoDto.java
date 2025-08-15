package org.common.dbiz.dto.paymentDto.einvoice;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BuyerInfoDto {

    String buyerPostalCode = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerLegalName = "";
    String buyerDistrictName = "";
    String buyerBankAccount = "";
    String buyerIdType = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerEmail = "";
    String buyerCityName = "";
    String buyerBirthDay = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerName = "";
    String buyerCode = "";
    String buyerCountryCode = "";
    String buyerFaxNumber = "";
    String buyerBankName = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerAddressLine = "";
    String buyerIdNo = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerTaxCode = "";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String buyerPhoneNumber = "";
}
