package com.dbiz.app.systemservice.domain;

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
@Builder
@Entity
@Table(name = "d_table_use_ref", schema = "pos")
public class TableUseRef extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_table_use_ref_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_table_use_ref_sq")
    @SequenceGenerator(name = "d_table_use_ref_sq", sequenceName = "d_table_use_ref_sq", allocationSize = 1)
    private BigDecimal id;

    @Size(max = 32)
    @Column(name = "domain_name", length = 32)
    private String domainName;

    @Size(max = 36)
    @Column(name = "d_table_use_ref_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String tableUseRefUu;

    @Column(name = "d_reference_id")
    private Integer referenceId;

    @Column(name = "d_org_id", precision = 10)
    private BigDecimal dOrgId;


    @Column(name = "domain_column")
    private String domainColumn;
    @Column(name = "d_tenant_id")
    private Integer tenantId;


}