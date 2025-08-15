package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "d_pricelist_org", schema = "pos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceListOrg extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pricelist_org_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pricelist_org_sq")
    @SequenceGenerator(name = "d_pricelist_org_sq", sequenceName = "d_pricelist_org_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "d_pricelist_id", nullable = false, precision = 10)
    private Integer pricelistId;


    @Size(max = 1)
    @Column(name = "isall", length = 1)
    private String isAll;



    @Size(max = 36)
    @Column(name = "d_pricelist_org_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String pricelistOrgUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

}