package com.dbiz.app.tenantservice.domain;

import com.dbiz.app.tenantservice.config.client.CustomAuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(CustomAuditingEntityListener.class)
@Table(name = "d_brand_partner", schema = "pos")
public class BrandPartner extends AbstractMappedEntity  implements Serializable {
    @Id
    @Column(name = "d_brand_partner_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_brand_partner_sq")
    @SequenceGenerator(name = "d_brand_partner_sq", sequenceName = "d_brand_partner_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "code", length = 15)
    private String code;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "image_code", length = 15)
    private String imageCode;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

}