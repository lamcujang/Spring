package com.dbiz.app.productservice.domain;

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
@Entity
@Builder
@Table(name = "d_uom", schema = "pos")
public class Uom  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_uom_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_uom_sq")
    @SequenceGenerator(name = "d_uom_sq", sequenceName = "d_uom_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 5)
    @Column(name = "code", length = 5)
    private String code;

    @Size(max = 15)
    @Column(name = "name", length = 15)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_uom_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String uomUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "erp_uom_id", precision = 10)
    private Integer erpUomId;

}