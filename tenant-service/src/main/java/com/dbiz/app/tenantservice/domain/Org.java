package com.dbiz.app.tenantservice.domain;

import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(CustomAuditingEntityListener.class)
@Table(name = "d_org", schema = "pos")
public class Org extends AbstractMappedEntity  implements Serializable {
    @Id
    @Column(name = "d_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_org_sq")
    @SequenceGenerator(name = "d_org_sq", sequenceName = "d_org_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;


    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;

    @Size(max = 15)
    @Column(name = "tax_code", length = 15)
    private String taxCode;

    @Size(max = 36)
    @Column(name = "d_org_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false, length = 36)
    private String orgUu;

    @Size(max = 64)
    @Column(name = "email", length = 64)
    private String email;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;


    @Size(max = 100)
    @Column(name = "area", length = 100)
    private String area; // khu vuc

    @Size(max = 100)
    @Column(name = "wards", length = 100)
    private String wards; // phuong


    @Column(name = "erp_org_id", precision = 10)
    private Integer erpOrgId;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 1)
    @Column(name = "is_summary", nullable = false, length = 1)
    private String isSummary = "N";

    @Column(name="d_tenant_id")
    private Integer tenantId;


    @Column(name = "is_pos_mng")
    private String isPosMng;

    @Column(name = "d_pricelist_id")
    private Integer priceListId;

    @Column(name = "ex_date")
    private Instant exDate;

    @Column(name = "d_image_id", precision = 10)
    private Integer imageId;

    @Size(max = 255)
    @Column(name = "country")
    private String country;

    @Column(name = "erp_org_name")
    private String erpOrgName;

}