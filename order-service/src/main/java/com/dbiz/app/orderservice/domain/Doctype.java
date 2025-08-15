package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_doctype", schema = "pos")
public class Doctype  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_doctype_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_doctype_sq")
    @SequenceGenerator(name = "d_doctype_sq", sequenceName = "d_doctype_sq", allocationSize = 1)

    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 36)
    @Column(name = "d_doctype_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String doctypeUu;

    @Column(name = "d_tenant_id" )
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}