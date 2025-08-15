package com.dbiz.app.orderservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_ro_lot", schema = "pos")
public class RoLot extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_ro_lot_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_ro_lot_sq")
    @SequenceGenerator(name = "d_ro_lot_sq", sequenceName = "d_ro_lot_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_return_order_id")
    private Integer returnOrderId;

    @Column(name = "d_return_orderline_id")
    private Integer returnOrderlineLineId;

    @Column(name = "d_lot_id")
    private Integer lotId;

    @Column(name = "qty")
    private BigDecimal qty;
}
