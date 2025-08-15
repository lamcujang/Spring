package com.dbiz.app.orderservice.domain;


import com.dbiz.app.orderservice.domain.view.UserKcV;
import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Entity
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "d_wds_header_v", schema = "pos")
public class WDSHeader extends AbstractMappedEntity implements Serializable {


    @Id
    @Column(name = "d_kitchen_order_id", precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Size(max = 32)
    @Column(name = "documentno", length = 32)
    private String documentno;

    @Column(name = "d_pos_order_id", precision = 10)
    private Integer posOrderId;

    @Column(name = "d_doctype_id", precision = 10)
    private Integer doctypeId;

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Size(max = 5)
    @Column(name = "order_status", length = 5)
    private String orderStatus;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "erp_kitchen_order_id", precision = 10)
    private Integer erpKitchenOrderId;

    @Size(max = 1)
    @Column(name = "is_sync_erp", length = 1)
    private String isSyncErp;


    @Column(name = "dateordered")
    private Instant dateordered;

    @Size(max = 64)
    @Column(name = "order_status_name", length = 64)
    private String orderStatusName;

    @Embedded
    UserKcV user;

    @Size(max = 255)
    @Column(name = "org_name")
    private String orgName;

    @Size(max = 32)
    @Column(name = "org_code", length = 32)
    private String orgCode;

    @Size(max = 32)
    @Column(name = "pos_document_no", length = 32)
    private String posDocumentNo;

    @Column(name = "warehouse_name" )
    private String warehouseName;
}
