package com.dbiz.app.tenantservice.domain;

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
@Table(name = "d_tenant_banner", schema = "pos")
public class TenantBanner extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_tenant_banner_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tenant_banner_sq")
    @SequenceGenerator(name = "d_tenant_banner_sq", sequenceName = "d_tenant_banner_sq", allocationSize = 1)
    private Integer id;

     @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Column(name = "d_image_id")
    private Integer imageId;


}