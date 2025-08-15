package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_attribute_value", schema = "pos")
public class AttributeValue extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_attribute_value_sq")
    @SequenceGenerator(name = "d_attribute_value_sq", sequenceName = "d_attribute_value_sq", allocationSize = 1)
    @Column(name = "d_attribute_value_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "value", length = 32)
    private String value;

    @Size(max = 32)
    @Column(name = "name", length = 32)
    private String name;

    @Size(max = 36)
    @Column(name = "d_attribute_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String attributeUu;

    @Column(name = "d_attribute_id")
    private Integer attributeId;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}