package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_reservation_line", schema = "pos")
public class ReservationLine  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_reservation_line_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_reservation_line_sq")
    @SequenceGenerator(name = "d_reservation_line_sq", sequenceName = "d_reservation_line_sq", allocationSize = 1)
    private Integer id;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "d_reservation_order_id", nullable = false, precision = 10)
    private Integer reservationOrderId;

    @NotNull
    @Column(name = "d_product_id", nullable = false, precision = 10)
    private BigDecimal productId;

    @NotNull
    @Column(name = "qty", nullable = false, precision = 10)
    private BigDecimal qty;

    @Column(name = "d_reservation_line_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String reservationLineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}