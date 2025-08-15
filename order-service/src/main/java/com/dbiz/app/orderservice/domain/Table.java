package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@javax.persistence.Table(name = "d_table", schema = "pos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Table extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_table_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_table_sq")
    @SequenceGenerator(name = "d_table_sq", sequenceName = "d_table_sq", allocationSize = 1)
    private Integer id;

    @NotNull
    @Column(name = "table_no", nullable = false, length = 5)
    private String tableNo;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "d_floor_id")
//    private Floor floor;
    @Column(name="d_floor_id")
    private Integer floorId;

    @Size(max = 3)
    @NotNull
    @Column(name = "table_status", nullable = false, length = 3)
    private String tableStatus;

    @Column(name = "number_seats")
    private Integer numberSeats;

    @Column(name = "display_index")
    private Integer displayIndex;

    @Size(max = 36)
    @Column(name = "d_table_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String tableUu;


    @Column(name = "number_guests", precision = 10)
    private BigDecimal numberGuests;

    @Size(max = 1)
    @Column(name = "is_room", length = 1)
    private String isRoom;

    @Column(name = "d_customer_id", precision = 10)
    private BigDecimal dCustomerId;

    @Size(max = 1)
    @Column(name = "is_locked", length = 1)
    private String isLocked;

    @Column(name = "erp_table_id", precision = 10)
    private Integer erpTableId;

    @Size(max = 1)
    @Column(name = "is_buffet", length = 1)
    private String isBuffet;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

}