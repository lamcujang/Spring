package org.common.dbiz.dto.paymentDto.einvoice;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SellerInfoDto {

    String sellerAddressLine = "";
    String sellerTaxCode= "";
    String sellerDistrictName= "";
    String sellerCityName= "";
    String sellerBankAccount= "";
    String sellerLegalName= "";
    String sellerFaxNumber= "";
    String sellerEmail= "";
    String sellerBankName= "";
    String sellerCountryCode= "";
    String sellerWebsite= "";
    String sellerPhoneNumber= "";
}
