package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_pos_orderline_detail", schema = "pos")
public class PosOrderLineDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_orderline_detail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_orderline_detail_sq")
    @SequenceGenerator(name = "d_pos_orderline_detail_sq", sequenceName = "d_pos_orderline_detail_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id", nullable = false)
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "d_pos_orderline_id", nullable = false)
    private Integer posOrderLineId;

    @NotNull
    @Column(name = "d_product_id", nullable = false)
    private Integer productId;

    @Column(name = "qty")
    private BigDecimal qty;

}