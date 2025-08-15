package com.dbiz.app.systemservice.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_version", schema = "pos")
public class Version extends AbstractMappedEntity{
    @Id
    @Column(name = "d_version_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_version_sq")
    @SequenceGenerator(name = "d_version_sq", sequenceName = "d_version_sq", allocationSize = 1)
    private BigDecimal id;

    @Column(name = "d_org_id", precision = 10)
    private BigDecimal dOrgId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "version")
    private String version;

    @Column(name = "min_version")
    private String minVersion;

    @Column(name = "store_url")
    private String storeUrl;

    @Column(name = "message")
    private String message;

    @Column(name = "platform")
    private String platform;

    @Column(name = "force_update")
    private String forceUpdate;
}
