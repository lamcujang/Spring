package com.dbiz.app.orderservice.domain.view;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_request_orderline_get_all_v", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrderLineGetAllV extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_request_orderline_id", precision = 10)
    private Integer id;


    @Embedded
    ProductV product;

    @Embedded
    UomV uom;




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
    @Column(name = "d_request_orderline_uu", length = 36)
    private String requestOrderLineUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;



    @ManyToOne
    @JoinColumn(name = "d_request_order_id", insertable = false, updatable = false)
    @JsonBackReference
    private RequestOrderGetAllV requestOrder;

    @Size(max = 64)
    @Column(name = "tax_name", length = 64)
    private String taxName;

    @Column(name = "tax_rate", precision = 10)
    private BigDecimal taxRate;

    @Column(name = "d_tax_id", precision = 10)
    private Integer taxId;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

}