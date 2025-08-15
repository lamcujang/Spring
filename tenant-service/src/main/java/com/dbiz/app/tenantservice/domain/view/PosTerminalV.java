package com.dbiz.app.tenantservice.domain.view;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "d_pos_terminal_v", schema = "pos")
public class PosTerminalV extends AbstractMappedEntity implements Serializable {

    @Id
    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer id;

    @Column(name = "d_tenant_id", precision = 10)
    private Integer tenantId;

    @Column(name = "d_org_id", precision = 10)
    private Integer orgId;

    @Size(max = 128)
    @Column(name = "terminal_name", length = 128)
    private String name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;
    
    @Size(max = 1)
    @Column(name = "is_restaurant", length = 1)
    private String isRestaurant;

    @Size(max = 64)
    @Column(name = "printer_ip", length = 64)
    private String printerIp;

    @Column(name = "printer_port", precision = 10)
    private Integer printerPort;

    @Size(max = 1)
    @Column(name = "is_bill_merge", length = 1)
    private String isBillMerge;

    @Size(max = 1)
    @Column(name = "is_notify_bill", length = 1)
    private String isNotifyBill;

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

    @Embedded
    private UserV user;

    @Embedded
    private BankAccountV bankAccount;

    @Embedded
    private BankAccountCashV bankAccountCash;

    @Embedded
    private BankAccountVisaV bankAccountVisa;

    @Embedded
    private PriceListV priceList;

    @Embedded
    private WarehouseV warehouse;

    @Embedded
    private DoctypeV doctype;

    @Column(name = "erp_pos_id", precision = 10)
    private Integer erpPosId;

}