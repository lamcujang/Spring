package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_note", schema = "pos")
public class Note extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_note_sq")
    @SequenceGenerator(name = "d_note_sq", sequenceName = "d_note_sq", allocationSize = 1)
    @Column(name = "d_note_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_note_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String noteUu;

    @Column(name = "d_note_group_id")
    private Integer noteGroupId;

    @Size(max = 255)
    @Column(name = "product_category_ids")
    private String productCategoryIds;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;
}