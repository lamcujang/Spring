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
@Table(name = "d_tax_agency_info", schema = "pos")
public class TaxAgencyInfo extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tax_agency_info_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_agency_info_sq")
    @SequenceGenerator(name = "d_tax_agency_info_sq", sequenceName = "d_tax_agency_info_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "old_tax_code", length = 20)
    private String oldTaxCode;

    // Tax Authority Info
    @Column(name = "region_tax_office", length = 255)
    private String regionTaxOffice;

    @Column(name = "managing_tax_office", length = 255)
    private String managingTaxOffice;

    // Tax Agent Info
    @Column(name = "tax_agent_code", length = 20)
    private String taxAgentCode;

    @Column(name = "tax_agent_name", length = 255)
    private String taxAgentName;

    @Column(name = "tax_agent_address", length = 255)
    private String taxAgentAddress;

    @Column(name = "tax_agent_city", length = 100)
    private String taxAgentCity;

    @Column(name = "tax_agent_ward", length = 100)
    private String taxAgentWard;

    @Column(name = "tax_agent_phone", length = 20)
    private String taxAgentPhone;

    @Column(name = "tax_agent_email", length = 100)
    private String taxAgentEmail;

    @Column(name = "tax_agent_contract_no", length = 50)
    private String taxAgentContractNo;

    @Column(name = "tax_agent_contract_date")
    private Date taxAgentContractDate;

    @Column(name = "tax_agent_staff_name", length = 255)
    private String taxAgentStaffName;

    @Column(name = "tax_agent_staff_cert_no", length = 50)
    private String taxAgentStaffCertNo;

    @Column(name = "tax_agent_show_on_report", length = 1)
    private String taxAgentShowOnReport;

    // Accounting Service Provider
    @Column(name = "accounting_cert_no", length = 50)
    private String accountingCertNo;

    @Column(name = "accounting_provider", length = 255)
    private String accountingProvider;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;
}