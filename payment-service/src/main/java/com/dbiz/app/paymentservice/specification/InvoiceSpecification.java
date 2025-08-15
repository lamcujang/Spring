package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.Invoice;
import com.dbiz.app.paymentservice.helper.DateHelper;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class InvoiceSpecification {

    public static Specification<Invoice> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Invoice> byCustomerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("customerId"), customerId);
    }

    public static Specification<Invoice> byVendorId(Integer vendorId) {
        return (root, query, criteriaBuilder) -> vendorId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("vendorId"), vendorId);
    }

    public static Specification<Invoice> byOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> orderId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orderId"), orderId);
    }

    public static Specification<Invoice> byDocumentNo(String documentNo) {
        return (root, query, criteriaBuilder) -> documentNo == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("documentNo"), documentNo);
    }

    public static Specification<Invoice> byDateInvoiced(String dateInvoiced) {
        return (root, query, criteriaBuilder) -> dateInvoiced == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("dateInvoiced"), DateHelper.toLocalDate(dateInvoiced));
    }

    public static Specification<Invoice> byCurrencyId(Integer currencyId) {
        return (root, query, criteriaBuilder) -> currencyId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("currencyId"), currencyId);
    }

    public static Specification<Invoice> byAccountingDate(String accountingDate) {
        return (root, query, criteriaBuilder) -> accountingDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("accountingDate"), DateHelper.toLocalDate(accountingDate));
    }

    public static Specification<Invoice> byBuyerName(String buyerName) {
        return (root, query, criteriaBuilder) -> buyerName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerName"), buyerName);
    }

    public static Specification<Invoice> byBuyerTaxCode(String buyerTaxCode) {
        return (root, query, criteriaBuilder) -> buyerTaxCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerTaxCode"), buyerTaxCode);
    }

    public static Specification<Invoice> byBuyerEmail(String buyerEmail) {
        return (root, query, criteriaBuilder) -> buyerEmail == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerEmail"), buyerEmail);
    }

    public static Specification<Invoice> byBuyerAddress(String buyerAddress) {
        return (root, query, criteriaBuilder) -> buyerAddress == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerAddress"), buyerAddress);
    }

    public static Specification<Invoice> byBuyerPhone(String buyerPhone) {
        return (root, query, criteriaBuilder) -> buyerPhone == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerPhone"), buyerPhone);
    }

    public static Specification<Invoice> byTotalAmount(BigDecimal totalAmount) {
        return (root, query, criteriaBuilder) -> totalAmount == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("totalAmount"), totalAmount);
    }

    public static Specification<Invoice> byInvoiceStatus(String invoiceStatus) {
        return (root, query, criteriaBuilder) -> invoiceStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceStatus"), invoiceStatus);
    }

    public static Specification<Invoice> byPriceListId(Integer priceListId) {
        return (root, query, criteriaBuilder) -> priceListId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("priceListId"), priceListId);
    }

    public static Specification<Invoice> byUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> userId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<Invoice> byReferenceInvoiceId(Integer referenceInvoiceId) {
        return (root, query, criteriaBuilder) -> referenceInvoiceId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("referenceInvoiceId"), referenceInvoiceId);
    }

    public static Specification<Invoice> byInvoiceForm(String invoiceForm) {
        return (root, query, criteriaBuilder) -> invoiceForm == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceForm"), invoiceForm);
    }

    public static Specification<Invoice> byInvoiceSign(String invoiceSign) {
        return (root, query, criteriaBuilder) -> invoiceSign == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceSign"), invoiceSign);
    }

    public static Specification<Invoice> byInvoiceNo(String invoiceNo) {
        return (root, query, criteriaBuilder) -> invoiceNo == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceNo"), invoiceNo);
    }

    public static Specification<Invoice> bySearchCode(String searchCode) {
        return (root, query, criteriaBuilder) -> searchCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("searchCode"), searchCode);
    }

    public static Specification<Invoice> bySearchLink(String searchLink) {
        return (root, query, criteriaBuilder) -> searchLink == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("searchLink"), searchLink);
    }

    public static Specification<Invoice> byInvoiceError(String invoiceError) {
        return (root, query, criteriaBuilder) -> invoiceError == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceError"), invoiceError);
    }

    private static Specification<Invoice> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<Invoice> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<Invoice> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<Invoice> getSpecification(InvoiceQueryRequest request) {
        return Specification.where(byId(request.getId()))
                .and(tenantId(AuditContext.getAuditInfo().getTenantId())
                .and(orgId(request.getOrgId()))
                .and(isActive(request.getIsActive()))
                .and(byCustomerId(request.getCustomerId()))
                .and(byVendorId(request.getVendorId()))
                .and(byOrderId(request.getOrderId()))
                .and(byDocumentNo(request.getDocumentNo()))
                .and(byDateInvoiced(request.getDateInvoiced()))
                .and(byCurrencyId(request.getCurrencyId()))
                .and(byAccountingDate(request.getAccountingDate()))
                .and(byBuyerName(request.getBuyerName()))
                .and(byBuyerTaxCode(request.getBuyerTaxCode()))
                .and(byBuyerEmail(request.getBuyerEmail()))
                .and(byBuyerAddress(request.getBuyerAddress()))
                .and(byBuyerPhone(request.getBuyerPhone()))
                .and(byTotalAmount(request.getTotalAmount()))
                .and(byInvoiceStatus(request.getInvoiceStatus()))
                .and(byPriceListId(request.getPriceListId()))
                .and(byUserId(request.getUserId()))
                .and(byReferenceInvoiceId(request.getReferenceInvoiceId()))
                .and(byInvoiceForm(request.getInvoiceForm()))
                .and(byInvoiceSign(request.getInvoiceSign()))
                .and(byInvoiceNo(request.getInvoiceNo()))
                .and(bySearchCode(request.getSearchCode()))
                .and(bySearchLink(request.getSearchLink()))
                .and(byInvoiceError(request.getInvoiceError())));
    }
}