package org.common.dbiz.dto.integrationDto.posOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PosInvoiceErpNextDto {
    @JsonProperty("docstatus")
    private Integer docstatus;
    private String title;
    private String customer;
    @JsonProperty("pos_profile")
    private String posProfile;
    @JsonProperty("is_pos")
    private Integer isPos;
    @JsonProperty("is_return")
    private Integer isReturn;
    @JsonProperty("update_billed_amount_in_sales_order")
    private Integer updateBilledAmountInSalesOrder;
    @JsonProperty("update_billed_amount_in_delivery_note")
    private Integer updateBilledAmountInDeliveryNote;
    private String company;
    @JsonProperty("posting_date")
    private String postingDate;
    @JsonProperty("set_posting_time")
    private Integer setPostingTime;
    @JsonProperty("due_date")
    private String dueDate;
    @JsonProperty("company_address")
    private String companyAddress;
    private String currency;
    @JsonProperty("selling_price_list")
    private String sellingPriceList;
    private String priceListCurrency;
    @JsonProperty("update_stock")
    private Integer updateStock;
    private Integer totalQty;
    private String baseInWords;
    private String accountForChangeAmount;
    private String inWords;
    private String writeOffAccount;
    private String writeOffCostCenter;
    private Integer groupSameItems;
    private String language;
    private Integer isDiscounted;
    private String status;
    private String debitTo;
    private String partyAccountCurrency;
    private String isOpening;
    private String remarks;
    private List<PaymentDto> payments;
    private List<ItemDto> items;
    private String description="";
    private List<TaxDto> taxes;
    @JsonProperty("d_pos_order_id")
    private String dPosOrderId;
    @JsonProperty("document_no")
    private String documentNo;
    @JsonProperty("set_warehouse")
    private String setWarehouse;
    @JsonProperty("sales_representative")
    private String salesRepresentative;
    @JsonProperty("account_date")
    private String accountDate;
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class PaymentDto {
        @JsonProperty("mode_of_payment")
        private String modeOfPayment;
        private BigDecimal amount;
        @JsonProperty("base_amount")
        private BigDecimal baseAmount;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class ItemDto {
        private String identifier;
        @JsonProperty("item_code")
        private String itemCode;
        @JsonProperty("item_name")
        private String itemName;
        private BigDecimal qty;
        private BigDecimal rate;
        private BigDecimal amount;
        private String description;
        @JsonProperty("income_account")
        private String incomeAccount;
        @JsonProperty("item_tax_template")
        private String itemTaxTemplate;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class TaxDto {
        @JsonProperty("charge_type")
        private String chargeType;
        @JsonProperty("account_head")
        private String accountHead;
        private String description;

    }


}
