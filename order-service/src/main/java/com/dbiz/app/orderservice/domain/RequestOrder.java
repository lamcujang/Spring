package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.PosTerminal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "d_request_order", schema = "pos")
public class RequestOrder extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_request_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_request_order_sq")
    @SequenceGenerator(name = "d_request_order_sq", sequenceName = "d_request_order_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_doctype_id")
    private Integer doctypeId;

    @Size(max = 32)
    @Column(name = "document_no", nullable = false, length = 32)
    private String documentNo;

    @Size(max = 5)
    @Column(name = "order_status", nullable = false, length = 5)
    private String orderStatus;

    @Column(name = "d_floor_id")
    private Integer floorId;

    @Column(name = "d_table_id")
    private Integer tableId;

    @Column(name = "order_time")
    private Instant orderTime;


    @Size(max = 36)
    @Column(name = "d_request_order_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String dRequestOrderUu;

    @Column(name = "d_tenant_id")
    private Integer tenantId ;

    @Column(name = "d_org_id")
    private Integer orgId;


    @Column(name = "d_customer_id")
    private Integer customerId;

    @Column(name = "d_price_list_id")
    private Integer priceListId;

    @Column(name = "d_pos_terminal_id")
    private Integer posTerminalId;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;

    @Size(max = 255)
    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "d_pos_order_id")
    private Integer posOrderId;
}