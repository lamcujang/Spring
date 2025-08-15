package com.dbiz.app.productservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
//@EqualsAndHashCode(callSuper = true, exclude = {"productCategory" , "uom","image"})
@Table(name = "d_warehouse", schema = "pos")
public class Warehouse extends AbstractMappedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_warehouse_sq")
    @SequenceGenerator(name = "d_warehouse_sq", sequenceName = "d_warehouse_sq", allocationSize = 1)
    @Column(name = "d_warehouse_id", nullable = false, precision = 10)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "address")
    private String address;

    @Size(max = 1)
    @Column(name = "is_negative", length = 1)
    private String isNegative;

    @Size(max = 36)
    @Column(name = "d_warehouse_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String warehouseUu;

    @Size(max = 100)
    @Column(name = "printer_ip", length = 100)
    private String printerIp;

    @Column(name = "d_tenant_id")
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Size(max = 255)
    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "erp_warehouse_id", precision = 10)
    private Integer erpWarehouseId;

    @Column(name = "printer_port")
    private Integer printerPort;

    @Column(name = "printer_type")
    private String printerType;

    @Column(name = "printer_name")
    private String printerName;

    @Column(name = "printer_product_id")
    private String printerProductId;

    @Column(name = "printer_vendor_id")
    private String printerVendorId;

    @Column(name = "printer_page_size")
    private Integer printerPageSize;

    @Column(name = "printer_page_so_qty")
    private Integer printerPageSoQty;

    @Column(name = "printer_page_temp_qty")
    private Integer printerPageTempQty;

    @Column(name = "erp_warehouse_name")
    private String erpWarehouseName;

    @Size(max = 1)
    @Column(name = "is_merge_item", length = 1)
    private String isMergeItem;
}