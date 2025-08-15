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
@Table(name = "get_kc_product_v", schema = "pos")
public class GetKcProductV {
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Id
    @Column(name = "d_product_id", precision = 10)
    private Integer productId;

    @Column(name = "created")
    private Instant created;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

}