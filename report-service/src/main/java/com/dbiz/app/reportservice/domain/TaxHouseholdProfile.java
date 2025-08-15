package com.dbiz.app.reportservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_tax_household_profile", schema = "pos")
public class TaxHouseholdProfile extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_household_profile_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "tax_auth_no", length = 50)
    private String taxAuthNo;

    @Column(name = "tax_auth_date")
    private Date taxAuthDate;

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

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @Column(name = "is_tax_authorized", nullable = false, length = 1)
    private String isTaxAuthorized;

    @Column(name = "is_border_trade", length = 1)
    private String isBorderTrade;

    @Column(name = "number_worker", precision = 6)
    private Integer numberWorker;

    @Column(name = "is_rent", nullable = false, length = 1)
    private String isRent;

    @Column(name = "store_area", precision = 6)
    private Integer storeArea;

    @Column(name = "finish_hour", precision = 2)
    private Integer finishHour;

    @Column(name = "finish_minute", precision = 2)
    private Integer finishMinute;

    @Column(name = "start_hour", precision = 2)
    private Integer startHour;

    @Column(name = "start_minute", precision = 2)
    private Integer startMinute;

    @Column(name = "resident_mobile", length = 20)
    private String residentMobile;

    @Column(name = "resident_fax", length = 50)
    private String residentFax;

    @Column(name = "resident_email", length = 100)
    private String residentEmail;

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

    @Column(name = "residence_address", length = 255)
    private String residenceAddress;

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

    @Column(name = "business_address", length = 255)
    private String businessAddress;
}