package org.common.dbiz.dto.reportDto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.common.dbiz.dto.reportDto.IndividualIndustryDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationIndividualInitResDto implements Serializable {

    private String businessProvinceName;
    private String businessProvinceCode;
    private String businessWardName;
    private String businessWardCode;
    private String businessAddress;

    private String taxAgentSignerName;

    private String isTaxAuthorized;
    private String taxAuthNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxAuthDate;
    private String proxyOrgName;
    private String proxyOrgCode;
    private String proxyOrgAddress;
    private String proxyOrgPhone;
    private String proxyOrgFax;
    private String proxyOrgEmail;

    private String isBorderTrade;

    private Integer startHour;
    private Integer startMinute;
    private Integer finishHour;
    private Integer finishMinute;

    private Integer numberWorker;
    private String isRent;
    private Integer storeArea;

    private List<IndividualIndustryDto> individualIndustryDto;
}