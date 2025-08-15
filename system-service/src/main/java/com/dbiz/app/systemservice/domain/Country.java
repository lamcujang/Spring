package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "d_country", schema = "pos")
public class Country extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_country_id", nullable = false, precision = 19)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_country_sq")
    @SequenceGenerator(name = "d_country_sq", sequenceName = "d_country_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 64)
    @NotNull
    @Column(name = "iso_country_code", nullable = false, length = 64)
    private String isoCountryCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Size(max = 64)
    @Column(name = "currency", length = 64)
    private String currency;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

}