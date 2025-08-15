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
@Table(name = "d_reference_list", schema = "pos")
public class ReferenceList    extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reference_list_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reference_list_sq")
    @SequenceGenerator(name = "d_reference_list_sq", sequenceName = "d_reference_list_sq", allocationSize = 1)

    private Integer id;


    @Column(name = "d_reference_id", nullable = false)
    private Integer referenceId;

    @Size(max = 15)
    @NotNull
    @Column(name = "value", nullable = false, length = 15)
    private String value;

    @Size(max = 64)
    @NotNull
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Size(max = 36)
    @Column(name = "d_reference_list_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String referenceListUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;


}