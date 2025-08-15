package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.dbiz.app.productservice.domain.view.OrgPriceListV;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "d_pricelist", schema = "pos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceList extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pricelist_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pricelist_sq")
    @SequenceGenerator(name = "d_pricelist_sq", sequenceName = "d_pricelist_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Size(max = 64)
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "from_date", nullable = false,  columnDefinition = "TIMESTAMP")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Instant  fromDate;

    @Column(name = "to_date",  columnDefinition = "TIMESTAMP")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Instant  toDate;

    @Size(max = 1)
    @Column(name = "is_saleprice", length = 1)
    private String isSaleprice;


    @Size(max = 36)
    @Column(name = "d_pricelist_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String pricelistUu;

    @Size(max = 1)
    @Column(name = "general_pricelist", columnDefinition = "CHAR(1) DEFAULT 'N'", insertable = false)
    private String generalPriceList;

    @OneToMany(mappedBy = "priceList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrgPriceListV>     priceListOrg;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_currency_id", precision = 10)
    private Integer currencyId;

    @Column(name = "price_precision")
    private Integer pricePrecision;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

    @Size(max = 5)
    @Column(name = "price_category_code", length = 5)
    private String priceCateCode;

    @Column(name = "erp_pricelist_id", precision = 10)
    private Integer erpPriceListId;

    @Column(name="erp_pricelist_name")
    private String erpPriceListName;

}