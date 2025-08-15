package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.dbiz.dto.reportDto.IndividualIndustryDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxHouseholdProfileDto implements Serializable {

    private Integer id;
    private String taxAuthNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxAuthDate;
    private String proxyOrgName;
    private String proxyOrgCode;
    private String proxyOrgAddress;
    private String proxyOrgProvince;
    private String proxyOrgDistrict;
    private String proxyOrgWard;
    private String proxyOrgPhone;
    private String proxyOrgFax;
    private String proxyOrgEmail;
    private String isActive;
    private String isTaxAuthorized;
    private String isBorderTrade;
    private Integer numberWorker;
    private String isRent;
    private Integer storeArea;
    private Integer finishHour;
    private Integer finishMinute;
    private Integer startHour;
    private Integer startMinute;
    private String residentMobile;
    private String residentFax;
    private String residentEmail;
    private String residenceProvinceName;
    private String residenceProvinceCode;
    private String residenceDistrictName;
    private String residenceDistrictCode;
    private String residenceWardName;
    private String residenceWardCode;
    private String residenceAddress;
    private String businessProvinceName;
    private String businessProvinceCode;
    private String businessDistrictName;
    private String businessDistrictCode;
    private String businessWardName;
    private String businessWardCode;
    private String businessAddress;
    private List<HouseholdIndustryDto> householdIndustryDto;
}