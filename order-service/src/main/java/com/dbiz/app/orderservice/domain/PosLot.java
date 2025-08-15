package com.dbiz.app.orderservice.domain;


import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_pos_lot", schema = "pos")
public class PosLot  extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_pos_lot_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_lot_sq")
    @SequenceGenerator(name = "d_pos_lot_sq", sequenceName = "d_pos_lot_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_pos_order_id")
    private Integer posOrderId;

    @Column(name = "d_pos_orderline_id")
    private Integer posOrderLineId;

    @Column(name = "d_lot_id")
    private Integer lotId;

    @Column(name = "saleprice")
    private BigDecimal salePrice;

    @Column(name = "qty")
    private BigDecimal qty;
}
