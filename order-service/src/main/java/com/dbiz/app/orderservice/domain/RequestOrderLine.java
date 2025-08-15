package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_request_orderline", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderLine extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_request_orderline_id", nullable = false, precision = 10)

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_request_orderline_sq")
    @SequenceGenerator(name = "d_request_orderline_sq", sequenceName = "d_request_orderline_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_product_id")
    private Integer productId;

    @Column(name = "d_request_order_id")
    private Integer requestOrderId;

    @Column(name = "qty", precision = 10)
    private BigDecimal qty;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Column(name = "saleprice", precision = 10, scale = 2)
    private BigDecimal saleprice;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 36)
    @Column(name = "d_request_orderline_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dRequestOrderlineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_tax_id")
    private Integer taxId;

    @Column(name = "d_pos_orderline_id")
    private Integer posOrderlineId;

}