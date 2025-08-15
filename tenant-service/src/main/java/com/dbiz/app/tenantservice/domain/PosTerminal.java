package com.dbiz.app.tenantservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "d_pos_terminal", schema = "pos")
public class PosTerminal extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_terminal_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_terminal_sq")
    @SequenceGenerator(name = "d_pos_terminal_sq", sequenceName = "d_pos_terminal_sq", allocationSize = 1)
    private Integer id;

    @Column(name = "d_tenant_id",updatable = false)
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_doctype_id")
    private Integer docTypeId;


    @Size(max = 128)
    @Column(name = "name", nullable = false, length = 128)
    private String name;


    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Column(name = "d_user_id", precision = 10)
    private Integer userId;

    @Column(name = "d_bank_account_cash_id", precision = 10)
    private Integer bankAccountCashId;

    @Size(max = 1)
    @Column(name = "is_restaurant", length = 1)
    private String isRestaurant;

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer priceListId;

    @Column(name = "d_warehouse_id", precision = 10)
    private Integer warehouseId;

    @Column(name = "d_bank_account_id", precision = 10)
    private Integer bankAccountId;

    @Size(max = 64)
    @Column(name = "printer_ip", length = 64)
    private String printerIp;

    @Column(name = "printer_port", precision = 10)
    private BigDecimal printerPort;

    @Size(max = 1)
    @Column(name = "is_bill_merge", length = 1)
    private String isBillMerge;

    @Size(max = 1)
    @Column(name = "is_notify_bill",columnDefinition = "varchar(1) default 'N'", length = 1)
    private String isNotifyBill;

    @Column(name = "d_bank_account_visa_id", precision = 10)
    private Integer bankAccountVisaId;

    @Column(name = "erp_pos_id", precision = 10)
    private Integer erpPosId;

    @Size(max = 36)
    @Column(name = "d_pos_terminal_uu",columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false, length = 36)
    private String dPosTerminalUu;


    @Size(max = 500)
    @Column(name = "device_token", length = 500)
    private String deviceToken;

    @Size(max = 1)
    @Column(name = "is_default", length = 1)
    private String isDefault;

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

    @Column(name="erp_ptm_name")
    private String erpPtmName;
}