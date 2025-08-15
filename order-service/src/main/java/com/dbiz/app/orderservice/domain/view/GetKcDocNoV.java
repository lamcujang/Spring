package com.dbiz.app.orderservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "get_kc_docno_v", schema = "pos")
public class GetKcDocNoV {
    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Size(max = 32)
    @Column(name = "documentno", length = 32)
    private String documentNo;

    @Id
    @Column(name = "d_kitchen_order_id", precision = 10)
    private Integer kitchenOrderId;

    @Column(name = "created")
    private Instant created;

}