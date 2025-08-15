package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.ReservationOrder;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.ShiftControl;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.ShiftControlQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class ShiftControlSpecification {
    private static Specification<ShiftControl> equalDate(String reservationTime) {
       return (root, query, criteriaBuilder) -> {
            if (reservationTime == null) {
                return criteriaBuilder.conjunction();
            }  Instant reservationInstant = DateHelper.toInstant(reservationTime);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("startDate"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
        };
    }

    public static Specification<ShiftControl> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<ShiftControl> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> (keyword == null || keyword == 0) ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<ShiftControl> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<ShiftControl> equalId(Integer PosClosedCashId)
    {
        return (root, query, criteriaBuilder) ->PosClosedCashId == null  ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("id"), PosClosedCashId);
    }

    public static Specification<ShiftControl> equalType(String shiftType)
    {
        return (root, query, criteriaBuilder) ->shiftType == null  ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("shiftType"), shiftType);
    }

    public static Specification<ShiftControl>equalPosTerminalId(Integer posTerminalId)
    {
        return (root, query, criteriaBuilder) ->posTerminalId == null  ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("posTerminalId"), posTerminalId);
    }

    public static Specification<ShiftControl> byDateBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) {
                return criteriaBuilder.conjunction();
            }

//            // Parse `fromDate` and `toDate` into Instant, defaulting `toDate` to `fromDate` if null
//            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = DateHelper.toInstant2((toDate != null ? toDate : fromDate) + " 23:59:59");
//
//            // Use `between` on `orderDate` with Instant values
//            Expression<Instant> orderDateExpression = root.get("startDate");
//            return criteriaBuilder.between(orderDateExpression, fromInstant, toInstant);

            List<Instant> range = DateHelper.fromDateToInstantRangeUTC(fromDate, toDate);
            Instant fromInstant = range.get(0);
            Instant toInstant = range.get(1);

            return criteriaBuilder.between(root.get("startDate"), fromInstant, toInstant);
        };
    }


    public static Specification<ShiftControl> getEntitySpecification(ShiftControlQueryRequest QueryRequest) {
        Specification<ShiftControl> spec = Specification.where(null);

       spec = spec.and(equalDate(QueryRequest.getCloseDate()));
         spec = spec.and(equalType(QueryRequest.getShiftType()));
        spec = spec.and(hasOrgId(QueryRequest.getOrgId()));
        spec = spec.and(equaIsActive("Y"));
        spec = spec.and(equalId(QueryRequest.getId()));
        spec = spec.and(equalPosTerminalId(QueryRequest.getPosTerminalId()));
        spec = spec.and(byDateBetween(QueryRequest.getFromDate(), QueryRequest.getToDate()));
        spec = spec.and(hasTenantId());
        return spec;
    }
}
