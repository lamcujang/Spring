package com.dbiz.app.userservice.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;

@Getter
@Setter
@Entity
@Table(name = "d_partner_group", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerGroup extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_partner_group_sq")
    @SequenceGenerator(name = "d_partner_group_sq", sequenceName = "d_partner_group_sq", allocationSize = 1)
    @Column(name = "d_partner_group_id", nullable = false, precision = 10)
    private Integer id;


    @Size(max = 32)
    @Column(name = "group_code", length = 32)
    private String groupCode;

    @Size(max = 128)
    @Column(name = "group_name", length = 128)
    private String groupName;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_partner_group_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String partnerGroupUu;

    @Size(max = 1)
    @Column(name = "is_customer", length = 1)
    private String isCustomer;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Size(max = 1)
    @Column(name = "is_summary", length = 1)
    private String isSummary;

    @Column(name = "d_partner_group_parent_id", precision = 10)
    private Integer dPartnerGroupParentId;

    @Column(name = "erp_bp_group_id", precision = 10)
    private Integer erpBpGroupId;

    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Column(name = "erp_bp_group_name", length = 255)
    private String erpBpGroupName;

}