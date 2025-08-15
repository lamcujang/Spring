package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_rq_orderline_detail", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
public class RqOrderlineDetail extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_rq_orderline_detail_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_rq_orderline_detail_sq")
    @SequenceGenerator(name = "d_rq_orderline_detail_sq", sequenceName = "d_rq_orderline_detail_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "qty", precision = 10)
    private BigDecimal qty;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Column(name = "saleprice", precision = 10, scale = 2)
    private BigDecimal saleprice;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;


    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_request_orderline_id")
    private Integer requestOrderLineId;

    @Column(name="d_product_id")
    private Integer productId;

}