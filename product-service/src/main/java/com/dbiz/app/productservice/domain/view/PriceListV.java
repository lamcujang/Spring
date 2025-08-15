package com.dbiz.app.productservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_price_list_v", schema = "pos")
public class PriceListV {
    @Id
    @Column(name = "d_pricelist_id", precision = 10)
    private Integer priceListId;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Size(max = 64)
    @Column(name = "name", length = 64)
    private String name;

    @Size(max = 1)
    @Column(name = "is_active", length = 1)
    private String isActive;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "to_date")
    private Instant toDate;

    @Size(max = 1)
    @Column(name = "is_saleprice", length = 1)
    private String isSaleprice;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Size(max = 36)
    @Column(name = "d_pricelist_uu", length = 36)
    private String dPricelistUu;

    @Size(max = 1)
    @Column(name = "general_pricelist", length = 1)
    private String generalPriceList;

    @Column(name = "d_currency_id", precision = 10)
    private Integer currencyId;

    @Column(name = "price_precision")
    private Integer pricePrecision;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

}