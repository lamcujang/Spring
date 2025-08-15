package com.dbiz.app.userservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_vendor", schema = "pos")
//@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
//@AuditLog
//@AuditTable(value = "d_vendor_audit")
public class Vendor extends AbstractMappedEntity  implements Serializable {
    @Id
    @Column(name = "d_vendor_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_vendor_sq")
    @SequenceGenerator(name = "d_vendor_sq", sequenceName = "d_vendor_sq", allocationSize = 1)
    private Integer id;



    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 15)
    @Column(name = "phone1", length = 15)
    private String phone1;

    @Size(max = 15)
    @Column(name = "phone2", length = 15)
    private String phone2;

    @Size(max = 255)
    @Column(name = "address1")
    private String address1;

    @Size(max = 255)
    @Column(name = "address2")
    private String address2;

    @Size(max = 15)
    @Column(name = "tax_code", length = 15)
    private String taxCode;

    @Size(max = 64)
    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;



//    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "d_image_id", referencedColumnName = "d_image_id", nullable = false)
    private Image image;



    @Size(max = 36)
    @Column(name = "d_vendor_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String vendorUu;


    @Column(name = "d_partner_group_id", precision = 10)
    private Integer partnerGroupId;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 100)
    @Column(name = "area", length = 100)
    private String area;

    @Size(max = 100)
    @Column(name = "wards", length = 100)
    private String wards;



    @Column(name="d_tenant_id")
    private Integer tenantId;

    @Column(name = "erp_vendor_id")
    private Integer erpVendorId;

    @Size(max = 1)
    @Column(name = "is_pos_vip", length = 1)
    private String isPosVip;

    @Size(max = 500)
    @Column(name = "partnername", length = 500)
    private String partnerName;

    @Column(name = "discount")
    private BigDecimal disCount;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;


    @Size(max = 1)
    @Column(name = "is_debt", length = 1)
    private String isDebt;

    @Size(max = 500)
    @Column(name = "company", length = 500)
    private String company;


    @Column(name = "city")
    private String city;

}