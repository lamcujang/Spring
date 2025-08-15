package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.view.PosOrderListView;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.PosOrderListQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

public class PosOrderListSpecification{
    public static Specification<PosOrderListView> hasDocNo(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("documentNo"), "%" + keyword + "%");
    }
    public static Specification<PosOrderListView> hasCustomerId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customerId"),  keyword);
    }
    public static Specification<PosOrderListView> orderDate(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderDate"), DateHelper.toLocalDate(keyword));
    }
    public static Specification<PosOrderListView> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<PosOrderListView> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<PosOrderListView> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }

    public static Specification<PosOrderListView> equaStatus(String[] keyword) {
        return (root, query, criteriaBuilder) -> root.get("orderStatus").in(keyword);
    }


    public static Specification<PosOrderListView> equalTableId(Integer tableId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableId"), tableId);
    }
    public static Specification<PosOrderListView> equalId(Integer PosOrderListViewId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), PosOrderListViewId);
    }

    public static Specification<PosOrderListView> equalFloorId(Integer floorId)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floorId"), floorId);
    }

    public static Specification<PosOrderListView> hasPriceList(Integer priceListId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priceListId"), priceListId);
    }

    public static Specification<PosOrderListView> hasShiftControl(Integer shiftControlId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("shiftControlId"), shiftControlId);
    }

    public static Specification<PosOrderListView> hasCusPhone(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("customerPhone"), "%" + keyword + "%");
    }

    public static Specification<PosOrderListView> hasCusName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<PosOrderListView> byDateInvoicedBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null || fromDate.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // 1. Older commented code

//            // If toDate is null or the same as fromDate, search only on fromDate
//            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = toDate == null || toDate.equals(fromDate)
//                    ? DateHelper.toInstant2(fromDate + " 23:59:59")
//                    : DateHelper.toInstant2(toDate + " 23:59:59");
//
////            Expression<Date> dateInvoiced = criteriaBuilder.function("DATE", Date.class, root.get("orderDate"));
////            return criteriaBuilder.between(dateInvoiced, Date.from(fromInstant), Date.from(toInstant));
//            return criteriaBuilder.between(root.get("orderDate"), Date.from(fromInstant), Date.from(toInstant));
//
//        };

            // Convert `orderDate` to a String in "yyyy-MM-dd" format for comparison
//            Expression<String> formattedDate = criteriaBuilder.function("to_char", String.class, root.get("orderDate"), criteriaBuilder.literal("yyyy-MM-dd"));
//
//            // Set `toDate` to `fromDate` if `toDate` is null, so it checks only on `fromDate`
//            String endDate = toDate != null ? toDate : fromDate;
//
//            // Use `between` with formatted dates as strings
//            return criteriaBuilder.between(formattedDate, fromDate, endDate);


            // 2. Old commented code, Error: parse passed time with HCM zone

            // Parse `fromDate` and `toDate` into Instant, defaulting `toDate` to `fromDate` if null
//            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = DateHelper.toInstant2((toDate != null ? toDate : fromDate) + " 23:59:59");
//
//            // Use `between` on `orderDate` with Instant values
//            Expression<Instant> orderDateExpression = root.get("orderDate");
//            return criteriaBuilder.between(orderDateExpression, fromInstant, toInstant);


            // 3. New code
            List<Instant> range = DateHelper.fromDateToInstantRangeUTC(fromDate, toDate);
            Instant fromInstant = range.get(0);
            Instant toInstant = range.get(1);

//            Path<Instant> orderDatePath = root.get("orderDate");
//            Predicate afterOrEq = criteriaBuilder.greaterThanOrEqualTo(orderDatePath, startInstant);
//            Predicate beforeOrEq = criteriaBuilder.lessThanOrEqualTo(orderDatePath, endInstant);
//            return criteriaBuilder.and(afterOrEq, beforeOrEq);

            return criteriaBuilder.between(root.get("orderDate"), fromInstant, toInstant);

        };
    }

    public static Specification<PosOrderListView> byOrderStatus(String orderStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), orderStatus);
    }


    public static Specification<PosOrderListView> getEntitySpecification(PosOrderListQueryRequest QueryRequest) {
        Specification<PosOrderListView> spec = Specification.where(null);

        spec = spec.and(hasTenantId());
        if(QueryRequest.getDocumentNo() != null){
            spec= spec.and(hasDocNo(QueryRequest.getDocumentNo()));
        }

//        if(QueryRequest.getOrderDate() != null){
//            spec= spec.and(orderDate(QueryRequest.getOrderDate()));
//        }
        if (QueryRequest.getOrgId()!= null){
            spec= spec.and(hasOrgId(QueryRequest.getOrgId()));
        }
        if(QueryRequest.getFromDate() != null ) {
            spec = spec.and(byDateInvoicedBetween(QueryRequest.getFromDate(), QueryRequest.getToDate()));
        }
        if(QueryRequest.getOrderStatus() != null){
            spec= spec.and(byOrderStatus(QueryRequest.getOrderStatus()));
        }

        if (QueryRequest.getPriceListId() != null) {
            spec = spec.and(hasPriceList(QueryRequest.getPriceListId()));
        }
        if (QueryRequest.getShiftControlId() != null) {
            spec = spec.and(hasShiftControl(QueryRequest.getShiftControlId()));
        }

        if(QueryRequest.getCustomerKeyword() != null){
            spec= spec.and(hasCusName(QueryRequest.getCustomerKeyword()).or(hasCusPhone(QueryRequest.getCustomerKeyword())));
        }



        return spec;
    }
}
