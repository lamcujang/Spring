package org.common.dbiz.dto.reportDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxAgencyInfoDto implements Serializable {
    private Integer id;
    private String oldTaxCode;

    // Tax Authority Info
    private String regionTaxOffice;
    private String managingTaxOffice;

    // Tax Agent Info
    private String taxAgentCode;
    private String taxAgentName;
    private String taxAgentAddress;
    private String taxAgentCity;
    private String taxAgentWard;
    private String taxAgentPhone;
    private String taxAgentEmail;
    private String taxAgentContractNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date taxAgentContractDate;

    private String taxAgentStaffName;
    private String taxAgentStaffCertNo;
    private String taxAgentShowOnReport;

    // Accounting Service Provider
    private String accountingCertNo;
    private String accountingProvider;
    private String isActive;
}