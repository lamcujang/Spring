package com.dbiz.app.productservice.domain.view;

import com.dbiz.app.productservice.domain.PriceList;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import java.math.BigDecimal;
@Getter
@Setter
@Table(name = "d_pricelist_org_info_v", schema = "pos")
@Entity
@Immutable
public class OrgPriceListV {



    @Id
    @Column(name = "d_org_id", precision = 10)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "d_pricelist_id", insertable = false, updatable = false)
    private PriceList priceList;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 32)
    @Column(name = "code", length = 32)
    private String code;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 100)
    @Column(name = "area", length = 100)
    private String area;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;



    @Size(max = 1)
    @Column(name = "isall", length = 1)
    private String isAll;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Size(max = 512)
    @Column(name = "address", length = 512)
    private String address;

}
