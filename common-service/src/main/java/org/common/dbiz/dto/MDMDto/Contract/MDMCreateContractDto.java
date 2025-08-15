package org.common.dbiz.dto.MDMDto.Contract;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MDMCreateContractDto {

    private String searchKey; // NULL is create
    private String contractName;
    private String businessOfLine;
    @JsonProperty("bPartnerSearchKey")
    private String bPartnerSearchKey; // INSERT BPartner Search Key
    private String startDate;
    private String productPackage; // SELECTION
    private String contractTermPackage; // SELECTION
    private String domain;
    private String email;
    private String status; // SELECTION
    private String active;
    private String attachment;
}
