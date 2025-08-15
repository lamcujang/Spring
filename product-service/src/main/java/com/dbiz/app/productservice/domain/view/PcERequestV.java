package com.dbiz.app.productservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_pc_e_request_v", schema = "pos")
public class PcERequestV {
    @Id
    @Column(name = "d_product_category_id", precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;


    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "d_product_category_parent_id", precision = 10)
    private Integer productCategoryParentId;

    @Size(max = 255)
    @Column(name = "parentname")
    private String parentName;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "index_sequence")
    private Integer indexSequence;

    @Column(name = "d_pos_terminal_id" )
    private Integer posTerminalId;


    @Column(name = "is_summary")
    private String isSummary;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Column(name="image_url")
    private String imageUrl;

}