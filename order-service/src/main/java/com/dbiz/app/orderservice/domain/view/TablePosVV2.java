package com.dbiz.app.orderservice.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.common.dbiz.helper.StringListToStringConverter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_table_pos_v_v2", schema = "pos")
public class TablePosVV2 implements Serializable {

    @Id
    @Column(name = "d_table_id", precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "created")
    private Instant created;

    @Column(name = "created_by", precision = 10)
    private Integer createdBy;

    @Column(name = "updated")
    private Instant updated;

    @Column(name = "updated_by", precision = 10)
    private Integer updatedBy;

    @Size(max = 32)
    @Column(name = "name", length = 32)
    private String name;

    @Size(max = 3)
    @Column(name = "table_status", length = 3)
    private String tableStatus;

    @Column(name = "display_index")
    private Integer displayIndex;

    @Column(name = "number_seats")
    private Integer numberSeats;

    @Size(max = 5)
    @Column(name = "table_no", length = 5)
    private String tableNo;

    @Column(name = "d_floor_id", precision = 10)
    private Integer floorId;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

    @Size(max = 1)
    @Column(name = "is_buffet", length = 1)
    private String isBuffet;

    @Size(max = 1)
    @Column(name = "is_room", length = 1)
    private String isRoom;

    @Column(name = "number_guests", precision = 10)
    private BigDecimal numberGuests;

    @Column(name = "floor_name")
    private String floorName;

    @Size(max = 1)
    @Column(name = "is_occupied", length = 1)
    private String isOccupied;

    @Column(name = "total_guests")
    private Integer totalGuests;

    @Convert(converter = StringListToStringConverter.class)
    @Column(name = "customer_names")
    private List<String> customerNames;

}
