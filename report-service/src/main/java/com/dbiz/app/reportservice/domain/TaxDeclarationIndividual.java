package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_declaration_individual", schema = "pos")
public class TaxDeclarationIndividual extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_declaration_individual_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_declaration_individual_sq")
    @SequenceGenerator(name = "d_tax_declaration_individual_sq", sequenceName = "d_tax_declaration_individual_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tax_payment_method_id", length = 1)
    private Integer taxPaymentMethodId;

    @Column(name = "d_tax_agency_info_id", length = 1)
    private Integer taxAgencyInfoId;

    @Column(name = "is_first_submission", length = 1)
    private String isFirstSubmission;

    @Column(name = "applicable_circular", length = 100)
    private String applicableCircular;

    @Column(name = "additional_submission", precision = 2)
    private Integer additionalSubmission;

    @Column(name = "taxpayer_name", length = 255)
    private String taxpayerName;

    @Column(name = "store_name", length = 255)
    private String storeName;

    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    @Column(name = "taxpayer_code", length = 25)
    private String taxpayerCode;

    @Column(name = "is_business_change", length = 1)
    private String isBusinessChange;

    @Column(name = "business_address", length = 255)
    private String businessAddress;

    @Column(name = "is_border_trade", length = 1)
    private String isBorderTrade;

    @Column(name = "is_address_change", length = 1)
    private String isAddressChange;

    @Column(name = "residence_address", length = 255)
    private String residenceAddress;

    @Column(name = "tax_agent_name", length = 255)
    private String taxAgentName;

    @Column(name = "tax_agent_code", length = 25)
    private String taxAgentCode;

    @Column(name = "proxy_org_name", length = 255)
    private String proxyOrgName;

    @Column(name = "proxy_org_code", length = 25)
    private String proxyOrgCode;

    @Column(name = "proxy_org_address", length = 255)
    private String proxyOrgAddress;

    @Column(name = "proxy_org_province", length = 100)
    private String proxyOrgProvince;

    @Column(name = "proxy_org_district", length = 100)
    private String proxyOrgDistrict;

    @Column(name = "proxy_org_ward", length = 100)
    private String proxyOrgWard;

    @Column(name = "proxy_org_phone", length = 25)
    private String proxyOrgPhone;

    @Column(name = "proxy_org_fax", length = 25)
    private String proxyOrgFax;

    @Column(name = "proxy_org_email", length = 50)
    private String proxyOrgEmail;

    @Column(name = "is_unregistered_individual", length = 1)
    private String isUnregisteredIndividual;

    @Column(name = "is_no_id_individual", length = 1)
    private String isNoIdIndividual;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @Column(name = "total_vat_revenue", precision = 20)
    private BigDecimal totalVatRevenue;

    @Column(name = "total_vat_amount", precision = 20)
    private BigDecimal totalVatAmount;

    @Column(name = "total_pit_revenue", precision = 20)
    private BigDecimal totalPitRevenue;

    @Column(name = "total_pit_amount", precision = 20)
    private BigDecimal totalPitAmount;

    @Column(name = "total_sed_revenue", precision = 20)
    private BigDecimal totalSedRevenue;

    @Column(name = "total_sed_amount", precision = 20)
    private BigDecimal totalSedAmount;

    @Column(name = "total_rtd_amount", precision = 20)
    private BigDecimal totalRtdAmount;

    @Column(name = "total_etd_amount", precision = 20)
    private BigDecimal totalEtdAmount;

    @Column(name = "total_efd_amount", precision = 20)
    private BigDecimal totalEfdAmount;

    @Column(name = "total_expenses", precision = 20)
    private BigDecimal totalExpenses;

    @Column(name = "tax_agent_staff_fullname", nullable = false, length = 255)
    private String taxAgentStaffFullname;

    @Column(name = "tax_agent_practice_certificate_no", length = 50)
    private String taxAgentPracticeCertificateNo;

    @Column(name = "tax_agent_signer_name", nullable = false, length = 255)
    private String taxAgentSignerName;

    @Column(name = "tax_agent_sign_date", nullable = false)
    private Date taxAgentSignDate;

    @Column(name = "number_worker", precision = 6)
    private Integer numberWorker;

    @Column(name = "is_rent", nullable = false, length = 1)
    private String isRent;

    @Column(name = "store_area", precision = 6)
    private Integer storeArea;

    @Column(name = "finish_hour", precision = 2)
    private Integer finishHour;

    @Column(name = "start_hour", precision = 2)
    private Integer startHour;

    @Column(name = "finish_minute", precision = 2)
    private Integer finishMinute;

    @Column(name = "resident_mobile", length = 20)
    private String residentMobile;

    @Column(name = "resident_fax", length = 50)
    private String residentFax;

    @Column(name = "resident_email", length = 100)
    private String residentEmail;

    @Column(name = "tax_auth_no", length = 50)
    private String taxAuthNo;

    @Column(name = "tax_auth_date")
    private Date taxAuthDate;

    @Column(name = "is_tax_authorized", nullable = false, length = 1)
    private String isTaxAuthorized;

    @Column(name = "start_minute", precision = 2)
    private Integer startMinute;

    @Column(name = "tax_period_type", length = 10)
    private String taxPeriodType;

    @Column(name = "tax_quarter", precision = 1)
    private Integer taxQuarter;

    @Column(name = "tax_year", precision = 4)
    private Integer taxYear;

    @Column(name = "tax_month_from")
    private Date taxMonthFrom;

    @Column(name = "tax_month_to")
    private Date taxMonthTo;

    @Column(name = "residence_province_name", length = 100)
    private String residenceProvinceName;

    @Column(name = "residence_province_code", length = 20)
    private String residenceProvinceCode;

    @Column(name = "residence_district_name", length = 100)
    private String residenceDistrictName;

    @Column(name = "residence_district_code", length = 20)
    private String residenceDistrictCode;

    @Column(name = "residence_ward_name", length = 100)
    private String residenceWardName;

    @Column(name = "residence_ward_code", length = 20)
    private String residenceWardCode;

    @Column(name = "business_province_name", length = 100)
    private String businessProvinceName;

    @Column(name = "business_province_code", length = 20)
    private String businessProvinceCode;

    @Column(name = "business_district_name", length = 100)
    private String businessDistrictName;

    @Column(name = "business_district_code", length = 20)
    private String businessDistrictCode;

    @Column(name = "business_ward_name", length = 100)
    private String businessWardName;

    @Column(name = "business_ward_code", length = 20)
    private String businessWardCode;

    @Column(name = "taxpayer_mobile", length = 20)
    private String taxpayerMobile;

    @Column(name = "taxpayer_fax", length = 50)
    private String taxpayerFax;

    @Column(name = "taxpayer_email", length = 100)
    private String taxpayerEmail;

    @Column(name = "taxpayer_province_name", length = 100)
    private String taxpayerProvinceName;

    @Column(name = "taxpayer_province_code", length = 20)
    private String taxpayerProvinceCode;

    @Column(name = "taxpayer_district_name", length = 100)
    private String taxpayerDistrictName;

    @Column(name = "taxpayer_district_code", length = 20)
    private String taxpayerDistrictCode;

    @Column(name = "taxpayer_ward_name", length = 100)
    private String taxpayerWardName;

    @Column(name = "taxpayer_ward_code", length = 20)
    private String taxpayerWardCode;

    @Column(name = "taxpayer_address", length = 255)
    private String taxpayerAddress;
}