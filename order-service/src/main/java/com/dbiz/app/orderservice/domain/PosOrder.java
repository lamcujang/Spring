package com.dbiz.app.orderservice.domain;

import com.dbiz.app.tenantservice.domain.AbstractMappedEntity;
import lombok.*;

import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "d_pos_order", schema = "pos")
public class  PosOrder  extends AbstractMappedEntity implements Serializable {
    @Id
    @Column(name = "d_pos_order_id", nullable = false, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "d_pos_order_sq")
    @SequenceGenerator(name = "d_pos_order_sq", sequenceName = "d_pos_order_sq", allocationSize = 1)
    private Integer id;



    @Column(name = "d_customer_id", nullable = false, precision = 10)
    private Integer customerId;

    @Size(max = 15)
    @Column(name = "phone", length = 15)
    private String phone;

    @Size(max = 5)
    @Column(name = "order_status", nullable = false, length = 5)
    private String orderStatus;

    @Size(max = 10)
    @Column(name = "source", nullable = false, length = 10)
    private String source;

    @Size(max = 1)
    @Column(name = "is_locked", nullable = false, length = 1)
    private String isLocked;

    @Column(name = "d_table_id", nullable = false, precision = 10)
    private Integer tableId;

    @Column(name = "d_floor_id", nullable = false, precision = 10)
    private Integer floorId;

    @Column(name = "d_user_id", nullable = false, precision = 10)
    private Integer userId;

    @Column(name = "order_guests")
    private Integer orderGuests;


    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Size(max = 255)
    @Column(name = "customer_name")
    private String customerName;


    @Size(max = 255)
    @Column(name = "qrcode_payment")
    private String qrcodePayment;

    @Size(max = 32)
    @Column(name = "document_no", nullable = false, length = 32)
    private String documentNo;

    @Column(name = "d_currency_id", nullable = false, precision = 10)
    private Integer currencyId;

    @Column(name = "d_pricelist_id", precision = 10)
    private Integer pricelistId;

    @Column(name = "d_pos_terminal_id", precision = 10)
    private Integer posTerminalId;

    @Column(name = "d_reconciledetail_id", precision = 10)
    private Integer reconcileDetailId;

    @Size(max = 255)
    @Column(name = "ftcode")
    private String ftCode;

    @Column(name = "d_bankaccount_id", precision = 10)
    private Integer bankAccountId;

    @Column(name = "d_bank_id", precision = 10)
    private Integer bankId;

    @Size(max = 36)
    @Column(name = "d_pos_order_uu" ,columnDefinition = "UUID DEFAULT uuid_generate_v4()", insertable = false, updatable = false)
    private String posOrderUu;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;


    @Column(name = "d_pos_id", precision = 10)
    private BigDecimal posId;

    @Column(name = "erp_pos_order_id", precision = 10)
    private Integer erpPosOrderId;

    @Size(max = 1)
    @Column(name = "is_applied_sercharge" ,insertable = false,columnDefinition = "default 'N'::character", length = 1)
    private String isAppliedSercharge;

    @Column(name = "flat_discount")
    private BigDecimal flatDiscount;

    @Size(max = 32)
    @Column(name = "erp_pos_order_no", length = 32)
    private String erpPosOrderNo;

    @Size(max = 64)
    @Column(name = "bill_no", length = 64)
    private String billNo;

    @Column(name = "d_shift_control_id", precision = 10)
    private Integer shiftControlId;

    @Size(max = 1)
    @Column(name = "is_processed",columnDefinition = "varchar(1)  default 'N'::character varying",insertable = false,updatable = true)
    private String isProcessed;

    @Size(max = 1)
    @Column(name = "is_sync_erp",insertable = false,columnDefinition = "default 'N'::character", length = 1,updatable = true)
    private String isSyncErp;

    @Column(name = "total_line")
    private BigDecimal totalLine;

    @Column(name = "d_doctype_id")
    private Integer doctypeId;

    @Column(name = "d_tenant_id" )
    private Integer tenantId;

    @Column(name = "d_org_id")
    private Integer orgId;

    @Column(name = "d_warehouse_id")
    private Integer warehouseId;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "d_cancel_reason_id")
    private Integer cancelReasonId;

    @Column(name = "cancel_reason_message")
    private String cancelReasonMessage;

    @Column(name = "d_request_order_id")
    private Integer requestOrderId;

    @Column(name = "flat_amt")
    private BigDecimal flatAmt;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "bankname")
    private String bankName;

    @Column(name = "bankowner")
    private String bankOwner;

    @Column(name = "d_image_id")
    private Integer imageId;

    @Column(name = "d_kitchen_order_id")
    private Integer kitchenOrderId;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "receipt_other_amt")
    private BigDecimal receiptOtherAmount;

    @Column(name = "d_np_transaction_id")
    private Integer npTransactionId;

    @Column(name = "d_np_order_id")
    private Integer npOrderId;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_tax_code")
    private String buyerTaxCode;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_address")
    private String buyerAddress;

    @Column(name = "buyer_company")
    private String buyerCompany;

    @Column(name = "buyer_phone")
    private String buyerPhone;

    @Size(max = 25)
    @Column(name = "buyer_citizen_id", length = 25)
    private String buyerCitizenId;

    @Size(max = 25)
    @Column(name = "buyer_passport_number")
    private String buyerPassportNumber;

    @Size(max = 25)
    @Column(name = "buyer_budget_unit_code")
    private String buyerBudgetUnitCode;

    @Column(name = "is_issue_einvoice")
    private String isIssueEInvoice;

    @Size(max = 5)
    @Column(name = "price_category_code", length = 5)
    private String priceCateCode;
}