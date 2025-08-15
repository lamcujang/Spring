package com.dbiz.app.tenantservice.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "d_org_banner", schema = "pos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgBanner extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_org_banner_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_org_banner_sq")
    @SequenceGenerator(name = "d_org_banner_sq", sequenceName = "d_org_banner_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "code")
    private String code;

    @Column(name = "d_image_id")
    private Integer imageId;


}