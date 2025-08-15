package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_reference", schema = "pos")
public class Reference  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reference_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reference_sq")
    @SequenceGenerator(name = "d_reference_sq", sequenceName = "d_reference_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Column(name = "d_reference_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String referenceUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

}