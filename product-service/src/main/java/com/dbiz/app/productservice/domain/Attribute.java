package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "d_attribute", schema = "pos")
public class Attribute  extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_attribute_sq")
    @SequenceGenerator(name = "d_attribute_sq", sequenceName = "d_attribute_sq", allocationSize = 1)
    @Column(name = "d_attribute_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 36)
    @Column(name = "d_attribute_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String attributeUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;



}