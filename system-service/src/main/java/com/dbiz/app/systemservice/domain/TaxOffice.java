package com.dbiz.app.systemservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "d_tax_office", schema = "pos")
public class TaxOffice extends AbstractMappedEntity {

    @Id
    @Column(name = "d_tax_office_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_tax_office_sq")
    @SequenceGenerator(name = "d_tax_office_sq", sequenceName = "d_tax_office_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false, precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", nullable = false, precision = 10)
    private Integer orgId;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "d_province_id", nullable = false, precision = 10)
    private Integer provinceId;

    @Column(name = "old_name", length = 255)
    private String oldName;

    @Column(name = "is_parent", length = 1)
    private String isParent;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "parent_code", length = 50)
    private String parentCode;

}
