package com.dbiz.app.paymentservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.paymentservice.domain.InvoiceView;
import com.dbiz.app.paymentservice.helper.DateHelper;
import org.common.dbiz.request.paymentRequest.InvoiceQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class InvoiceViewSpecification {

    public static Specification<InvoiceView> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<InvoiceView> byCustomerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("customerId"), customerId);
    }

    public static Specification<InvoiceView> byVendorId(Integer vendorId) {
        return (root, query, criteriaBuilder) -> vendorId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("vendorId"), vendorId);
    }

    public static Specification<InvoiceView> byOrderId(Integer orderId) {
        return (root, query, criteriaBuilder) -> orderId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orderId"), orderId);
    }

    public static Specification<InvoiceView> byDocumentNo(String documentNo) {
        return (root, query, criteriaBuilder) -> documentNo == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("documentNo")), "%" + documentNo.toLowerCase() + "%");
    }

    public static Specification<InvoiceView> byDocumentNoOrPosOrderNo(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // không lọc gì nếu null hoặc rỗng
            }

            String likePattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("documentNo")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("posOrderNo")), likePattern)
            );
        };
    }

    public static Specification<InvoiceView> byDateInvoiced(String dateInvoiced) {
        return (root, query, criteriaBuilder) -> dateInvoiced == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("dateInvoiced"), DateHelper.toLocalDate(dateInvoiced));
    }

    public static Specification<InvoiceView> byCurrencyId(Integer currencyId) {
        return (root, query, criteriaBuilder) -> currencyId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("currencyId"), currencyId);
    }

    public static Specification<InvoiceView> byAccountingDate(String accountingDate) {
        return (root, query, criteriaBuilder) -> accountingDate == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("accountingDate"), DateHelper.toLocalDate(accountingDate));
    }

    public static Specification<InvoiceView> byBuyerName(String buyerName) {
        return (root, query, criteriaBuilder) -> buyerName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerName"), buyerName);
    }

    public static Specification<InvoiceView> byBuyerTaxCode(String buyerTaxCode) {
        return (root, query, criteriaBuilder) -> buyerTaxCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerTaxCode"), buyerTaxCode);
    }

    public static Specification<InvoiceView> byBuyerEmail(String buyerEmail) {
        return (root, query, criteriaBuilder) -> buyerEmail == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerEmail"), buyerEmail);
    }

    public static Specification<InvoiceView> byBuyerAddress(String buyerAddress) {
        return (root, query, criteriaBuilder) -> buyerAddress == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerAddress"), buyerAddress);
    }

    public static Specification<InvoiceView> byBuyerPhone(String buyerPhone) {
        return (root, query, criteriaBuilder) -> buyerPhone == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("buyerPhone"), buyerPhone);
    }

    public static Specification<InvoiceView> byTotalAmount(BigDecimal totalAmount) {
        return (root, query, criteriaBuilder) -> totalAmount == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("totalAmount"), totalAmount);
    }

    public static Specification<InvoiceView> byInvoiceStatus(String invoiceStatus) {
        return (root, query, criteriaBuilder) -> invoiceStatus == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceStatus"), invoiceStatus);
    }

    public static Specification<InvoiceView> byPriceListId(Integer priceListId) {
        return (root, query, criteriaBuilder) -> priceListId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("priceListId"), priceListId);
    }

    public static Specification<InvoiceView> byUserId(Integer userId) {
        return (root, query, criteriaBuilder) -> userId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<InvoiceView> byReferenceInvoiceId(Integer referenceInvoiceId) {
        return (root, query, criteriaBuilder) -> referenceInvoiceId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("referenceInvoiceId"), referenceInvoiceId);
    }

    public static Specification<InvoiceView> byInvoiceForm(String invoiceForm) {
        return (root, query, criteriaBuilder) -> invoiceForm == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceForm"), invoiceForm);
    }

    public static Specification<InvoiceView> byInvoiceSign(String invoiceSign) {
        return (root, query, criteriaBuilder) -> invoiceSign == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceSign"), invoiceSign);
    }

    public static Specification<InvoiceView> byInvoiceNo(String invoiceNo) {
        return (root, query, criteriaBuilder) -> invoiceNo == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("invoiceNo")), "%" + invoiceNo.toLowerCase() + "%");
    }

    public static Specification<InvoiceView> bySearchCode(String searchCode) {
        return (root, query, criteriaBuilder) -> searchCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("searchCode"), searchCode);
    }

    public static Specification<InvoiceView> bySearchLink(String searchLink) {
        return (root, query, criteriaBuilder) -> searchLink == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("searchLink"), searchLink);
    }

    public static Specification<InvoiceView> byInvoiceError(String invoiceError) {
        return (root, query, criteriaBuilder) -> invoiceError == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceError"), invoiceError);
    }
    public static Specification<InvoiceView> byPhone(String phone) {
        return (root, query, criteriaBuilder) -> phone == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("customerPhone"), phone);
    }
    private static Specification<InvoiceView> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    private static Specification<InvoiceView> tenantId(Integer tenantId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), tenantId);
    }

    private static Specification<InvoiceView> orgId(Integer orgId) {

//        return (root, query, criteriaBuilder) -> (orgId == null || orgId != 0) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orgId"), orgId);
        return (root, query, criteriaBuilder) -> (orgId == null) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("orgId"), orgId);

    }

    public static Specification<InvoiceView> hasPriceList(Integer priceListId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priceListId"), priceListId);
    }

    private static Specification<InvoiceView> posOrderNo(String docNo) {
        return (root, query, criteriaBuilder) -> docNo == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("posOrderNo")), "%" + docNo.toLowerCase() + "%");
    }

    private static Specification<InvoiceView> likeCustomerName(String cusName) {
        return (root, query, criteriaBuilder) -> cusName == null ? criteriaBuilder.conjunction() : criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), "%" + cusName.toLowerCase() + "%");
    }

    public static Specification<InvoiceView> hasCusPhone(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("customerPhone"), "%" + keyword + "%");
    }

    public static Specification<InvoiceView> hasCusName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), "%" + keyword.toLowerCase() + "%");
    }


//    private static Specification<InvoiceView> dateInvoice(String reservationTime) {
////        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
//        return (root, query, criteriaBuilder) -> {
//            if (reservationTime == null) {
//                return null;
//            }
//            Instant reservationInstant = DateHelper.toInstant(reservationTime);
//            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("dateInvoiced"));
//            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
//            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
//        };
//    }

    public static Specification<InvoiceView> byDateInvoicedBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null || fromDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // 1. Oldest code
//            // If toDate is null or the same as fromDate, search only on fromDate
//            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = toDate == null || toDate.equals(fromDate)
//                    ? DateHelper.toInstant2(fromDate + " 23:59:59")
//                    : DateHelper.toInstant2(toDate + " 23:59:59");
//
//            Expression<Date> dateInvoiced = criteriaBuilder.function("DATE", Date.class, root.get("dateInvoiced"));
//            return criteriaBuilder.between(dateInvoiced, Date.from(fromInstant), Date.from(toInstant));

            // 2.
//            Instant fromInstant = org.common.dbiz.helper.DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = org.common.dbiz.helper.DateHelper.toInstant2((toDate != null ? toDate : fromDate) + " 23:59:59");
//
//            // Use `between` on `orderDate` with Instant values
//            Expression<Instant> orderDateExpression = root.get("dateInvoiced");
//            return criteriaBuilder.between(orderDateExpression, fromInstant, toInstant);

            // 3.
            List<Instant> range = org.common.dbiz.helper.DateHelper.fromDateToInstantRangeUTC(fromDate, toDate);
            Instant fromInstant = range.get(0);
            Instant toInstant = range.get(1);

            return criteriaBuilder.between(root.get("dateInvoiced"), fromInstant, toInstant);

        };
    }


    private static Specification<InvoiceView> invoiceStatus(String status) {
        return (root, query, criteriaBuilder) -> status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("invoiceStatus"), (status));
    }

    public static Specification<InvoiceView> getSpecification(InvoiceQueryRequest request) {

        Specification <InvoiceView> specification = Specification.where(null);
        specification = specification.and(tenantId(AuditContext.getAuditInfo().getTenantId()));
        specification = specification.and(posOrderNo(request.getPosOrderNo()));
        specification = specification.and(orgId(request.getOrgId()));
//        specification = specification.and(likeCustomerName(request.getCustomerName()));
//        specification=specification.or(byPhone(request.getCustomerName()));
        specification = specification.and(isActive(request.getIsActive()));
//        specification = specification.and(dateInvoice(request.getDateInvoiced()));
        specification = specification.and(byDateInvoicedBetween(request.getFromDate(), request.getToDate()));
        specification = specification.and(invoiceStatus(request.getInvoiceStatus()));
//        specification = specification.and(byDocumentNo(request.getDocumentNo()));
        specification = specification.and(byDocumentNoOrPosOrderNo(request.getDocumentNo()));
//        specification=specification.and(byBuyerPhone(request.getBuyerPhone()));

        if(request.getCustomerKeyword() != null){
            specification= specification.and(hasCusName(request.getCustomerKeyword()).or(hasCusPhone(request.getCustomerKeyword())));
        }
        if(request.getPriceListId() != null){
            specification = specification.and(hasPriceList(request.getPriceListId()));
        }

        return specification;
    }
}