package org.common.dbiz.dto.MDMDto.BPartner;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MDMCreateBPartnerDto {

    private String searchKey; // NULL is create
    @JsonProperty("bPartnerName")
    private String bPartnerName;
    private String address;
    private String state;
    private String country;
    private String taxCode;
    private String phone;
    private String email;
    private String representative;
    private List<String> typeOfBusiness; // SELECTION
    private List<String> industrySector; // SELECTION
    private String numberOfTables; // SELECTION
    private String numberOfEmployees; // SELECTION
    private String registerDate;
    private String active;
    private String logo;
    private String salesRepresentative;
    private String googleId;
    private String facebookId;
    private String zaloId;
    private String lineId;
    private String tiktokId;


}
