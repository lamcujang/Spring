package com.dbiz.app.orderservice.domain.view;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
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
@Table(name = "d_request_order_get_all_v", schema = "pos")
public class RequestOrderGetAllV extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_request_order_id", precision = 10)
    private Integer id;

    @Embedded
    CustomerV customer;

    @Column(name = "order_time")
    private Instant orderTime;

    @Embedded
    TableV table;

    @Embedded
    FloorV floor;

    @Size(max = 5)
    @Column(name = "order_status", length = 5)
    private String orderStatus;

    @Size(max = 36)
    @Column(name = "d_request_order_uu", length = 36)
    private String requestOrderUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @OneToMany(mappedBy = "requestOrder", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<RequestOrderLineGetAllV> requestOrderLines;

    @Column(name = "d_price_list_id", precision = 10)
    private Integer priceListId;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "status_name")
    private String statusName;

}