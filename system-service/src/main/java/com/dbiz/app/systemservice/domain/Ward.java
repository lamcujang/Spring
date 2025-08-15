package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_ward", schema = "pos")
public class Ward extends AbstractMappedEntity {

    @Id
    @Column(name = "d_ward_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_ward_sq")
    @SequenceGenerator(name = "d_ward_sq", sequenceName = "d_ward_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false, precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "d_province_id", nullable = false, precision = 10)
    private Integer provinceId;

    @Size(max = 1)
    @Column(name = "has_merger", length = 1)
    private String hasMerger;

    @Column(name = "merger_details")
    private String mergerDetails;

}
