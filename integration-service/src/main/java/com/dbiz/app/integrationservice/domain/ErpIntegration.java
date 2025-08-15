package com.dbiz.app.integrationservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_erp_integration", schema = "pos")
public class ErpIntegration extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_erp_integration_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_erp_integration_sq")
    @SequenceGenerator(name = "d_erp_integration_sq", sequenceName = "d_erp_integration_sq", allocationSize = 1)
    private Integer id;


    @Size(max = 32)
    @Column(name = "erp_platform", length = 32)
    private String erpPlatform;

    @Size(max = 128)
    @NotNull
    @Column(name = "erp_url", nullable = false, length = 128)
    private String erpUrl;

    @Column(name = "ad_client_id", precision = 10)
    private Integer adClientId;

    @Column(name = "ad_org_id", precision = 10)
    private Integer adOrgId;

    @Column(name = "ad_role_id", precision = 10)
    private Integer adRoleId;

    @Column(name = "m_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Size(max = 32)
    @Column(name = "username", length = 32)
    private String username;

    @Size(max = 32)
    @Column(name = "password", length = 32)
    private String password;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 1)
    @NotNull
    @Column(name = "is_default", nullable = false, length = 1)
    private String isDefault;

    @Size(max = 3)
    @NotNull
    @Column(name = "bankaccount_type", nullable = false, length = 3)
    private String bankAccountType;

    @Size(max = 36)
    @Column(name = "d_erp_integration_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String erpIntegrationUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_secret")
    private String apiSecret;

    @Column(name="integration_type")
    private String integrationType;

    @Column(name="cookies")
    private String cookies;
}