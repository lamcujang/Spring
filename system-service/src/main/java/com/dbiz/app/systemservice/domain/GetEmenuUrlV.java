package com.dbiz.app.systemservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "get_emenu_url_v", schema = "pos")
public class GetEmenuUrlV {
    @Id
    @Column(name = "d_table_id", precision = 10)
    private Integer tableId;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "emenuurl")
    @Type(type = "org.hibernate.type.TextType")
    private String emenuUrl;

    @Size(max = 20)
    @Column(name = "floor_no", length = 20)
    private String floorNo;

    @Size(max = 255)
    @Column(name = "floor_name")
    private String floorName;

    @Size(max = 32)
    @Column(name = "tabel_name", length = 32)
    private String tableName;

    @Size(max = 5)
    @Column(name = "table_no", length = 5)
    private String tableNo;



    @Column(name = "d_floor_id", precision = 10)
    private Integer floorId;



    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Size(max = 255)
    @Column(name = "org_name")
    private String orgName;

    @Column(name = "created")
    private String created;

    @Column(name="d_pricelist_id")
    private Integer priceListId;

    @Column(name = "address")
    private String address;
}