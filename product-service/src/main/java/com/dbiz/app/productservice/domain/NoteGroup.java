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
@Table(name = "d_note_group", schema = "pos")
public class NoteGroup extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_note_group_sq")
    @SequenceGenerator(name = "d_note_group_sq", sequenceName = "d_note_group_sq", allocationSize = 1)
    @Column(name = "d_note_group_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @Column(name = "group_name", nullable = false, length = 32)
    private String groupName;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 36)
    @Column(name = "d_note_group_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String noteGroupUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;
}