package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_config", schema = "pos")
public class Config extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_config_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_org_sq")
    @SequenceGenerator(name = "d_org_sq", sequenceName = "d_org_sq", allocationSize = 1)
    private Integer id;



    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 512)
    @NotNull
    @Column(name = "value", nullable = false, length = 512)
    private String value;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Size(max = 36)
    @NotNull
    @Column(name = "d_config_uu", nullable = false, length = 36)
    private String configUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

}