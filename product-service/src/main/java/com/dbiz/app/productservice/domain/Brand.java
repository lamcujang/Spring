package com.dbiz.app.productservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_brand", schema = "pos")
public class Brand extends AbstractMappedEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_brand_sq")
    @SequenceGenerator(name = "d_brand_sq", sequenceName = "d_brand_sq", allocationSize = 1)
    @Column(name = "d_brand_id", nullable = false, precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

}
