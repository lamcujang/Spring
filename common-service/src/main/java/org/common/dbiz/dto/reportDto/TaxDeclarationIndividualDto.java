package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxDeclarationIndividualDto implements Serializable {

    private Integer id;
    private Integer taxPaymentMethodId;
    private Integer taxAgencyInfoId;
    private String applicableCircular;
    private String isFirstSubmission;
    private Integer additionalSubmission;
    private String taxpayerName;
    private String storeName;
    private String bankAccount;
    private String taxpayerCode;
    private String isBusinessChange;

    private String businessProvinceName;
    private String businessProvinceCode;
    private String businessDistrictName;
    private String businessDistrictCode;
    private String businessWardName;
    private String businessWardCode;
    private String businessAddress;

    private String isBorderTrade;
    private String isAddressChange;
    private String taxAgentName;
    private String taxAgentCode;
    private String proxyOrgName;
    private String proxyOrgCode;
    private String proxyOrgAddress;
    private String proxyOrgProvince;
    private String proxyOrgDistrict;
    private String proxyOrgWard;
    private String proxyOrgPhone;
    private String proxyOrgFax;
    private String proxyOrgEmail;
    private String isUnregisteredIndividual;
    private String isNoIdIndividual;
    private String isActive;
    private BigDecimal totalVatRevenue;
    private BigDecimal totalVatAmount;
    private BigDecimal totalPitRevenue;
    private BigDecimal totalPitAmount;
    private BigDecimal totalSedRevenue;
    private BigDecimal totalSedAmount;
    private BigDecimal totalRtdAmount;
    private BigDecimal totalEtdAmount;
    private BigDecimal totalEfdAmount;
    private BigDecimal totalExpenses;
    private String taxAgentStaffFullname;
    private String taxAgentPracticeCertificateNo;
    private String taxAgentSignerName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxAgentSignDate;
    private Integer numberWorker;
    private String isRent;
    private Integer storeArea;
    private Integer finishHour;
    private Integer startHour;
    private Integer finishMinute;

    private String residenceProvinceName;
    private String residenceProvinceCode;
    private String residenceDistrictName;
    private String residenceDistrictCode;
    private String residenceWardName;
    private String residenceWardCode;
    private String residenceAddress;

    private String residentMobile;
    private String residentFax;
    private String residentEmail;
    private String taxAuthNo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxAuthDate;
    private String isTaxAuthorized;
    private Integer startMinute;
    private String taxPeriodType;
    private Integer taxQuarter;
    private Integer taxYear;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxMonthFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxMonthTo;

    private String taxpayerMobile;
    private String taxpayerFax;
    private String taxpayerEmail;

    private String taxpayerProvinceName;
    private String taxpayerProvinceCode;
    private String taxpayerDistrictName;
    private String taxpayerDistrictCode;
    private String taxpayerWardName;
    private String taxpayerWardCode;
    private String taxpayerAddress;

    private TaxAgencyInfoDto taxAgencyInfoDto;
    private List<IndividualIndustryDto> individualIndustryDto;
}