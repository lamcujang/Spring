package com.dbiz.app.productservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_product_e_request_v", schema = "pos")
public class ProductERequestV {

    @Id
    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;


    @Column(name = "d_product_category_id")
    private Integer productCategoryId;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "salesprice")
    private BigDecimal salesPrice;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Column(name = "created")
    private Instant created;

    @Column(name = "updated")
    private Instant updated;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "d_tax_id")
    private Integer taxId;

}