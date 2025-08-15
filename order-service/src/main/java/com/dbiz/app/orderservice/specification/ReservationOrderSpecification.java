package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.view.PosOrderListView;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.ReservationOrder;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReservationOrderSpecification {

    private static Specification<ReservationOrder> id(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    private static Specification<ReservationOrder> customerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? null : criteriaBuilder.equal(root.get("customerId"), customerId);
    }

    private static Specification<ReservationOrder> ReservationOrderDate(String reservationTime) {
//        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
        return (root, query, criteriaBuilder) -> {
            if (reservationTime == null) {
                return null;
            }  Instant reservationInstant = DateHelper.toInstant(reservationTime);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("reservationTime"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
        };
    }




    public static Specification<ReservationOrder> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    private static Specification<ReservationOrder> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<ReservationOrder> floorId(Integer floorId) {
        return (root, query, criteriaBuilder) -> floorId == null ? null : criteriaBuilder.equal(root.get("floorId"), floorId);
    }

    private static Specification<ReservationOrder> tableId(Integer tableId) {
        return (root, query, criteriaBuilder) -> tableId == null ? null : criteriaBuilder.equal(root.get("tableId"), tableId);
    }

    private static Specification<ReservationOrder> status(String[] status) {
        List<String> statusList = Arrays.asList(status);
        return (root, query, criteriaBuilder) -> root.get("status").in(statusList);
    }

    private static Specification<ReservationOrder>customerName(String customerName) {
        return (root, query, criteriaBuilder) -> customerName == null ? null : criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%");
    }

//    private static Specification<ReservationOrder>fromDate (String date)
//    {
//        return (root, query, criteriaBuilder) -> date == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("reservationTime"), DateHelper.toInstant(date));
//    }
//    private static Specification<ReservationOrder>toDate (String date)
//    {
//        return (root, query, criteriaBuilder) -> date == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThanOrEqualTo(root.get("reservationTime"), DateHelper.toInstant(date));
//    }

    public static Specification<ReservationOrder> byDateBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) {
                return criteriaBuilder.conjunction();
            }

//            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
//            Instant toInstant = DateHelper.toInstant2((toDate != null ? toDate : fromDate) + " 23:59:59");
//            // Use `between` on `orderDate` with Instant values
//            Expression<Instant> orderDateExpression = root.get("reservationTime");
//            return criteriaBuilder.between(orderDateExpression, fromInstant, toInstant);

            List<Instant> range = DateHelper.fromDateToInstantRangeUTC(fromDate, toDate);
            Instant fromInstant = range.get(0);
            Instant toInstant = range.get(1);
            return criteriaBuilder.between(root.get("reservationTime"), fromInstant, toInstant);

        };
    }


    public static Specification<ReservationOrder> getEntitySpecification(ReservationOrderQueryRequest entityQueryRequest) {
        Specification<ReservationOrder> spec = Specification.where(null);
        spec = spec.and(hasTenantId());
        if(entityQueryRequest.getReservationTime() != null){
            spec= spec.and(ReservationOrderDate(entityQueryRequest.getReservationTime()));
        }
        if(entityQueryRequest.getTableId() != null){
            spec= spec.and(tableId(entityQueryRequest.getTableId()));
        }
        if(entityQueryRequest.getFloorId()!= null){
            spec= spec.and(floorId(entityQueryRequest.getFloorId()));
        }
        if(entityQueryRequest.getOrgId()!= null)
        {
            spec= spec.and(orgId(entityQueryRequest.getOrgId()));
        }
        if(entityQueryRequest.getCustomerId()!=null)
        {
            spec= spec.and(customerId(entityQueryRequest.getCustomerId()));
        }
        if(entityQueryRequest.getStatus()!=null)
        {
            spec= spec.and(status(entityQueryRequest.getStatus()));
        }
        if(entityQueryRequest.getCustomerName()!= null)
        {
            spec= spec.and(customerName(entityQueryRequest.getCustomerName()));
        }

//        spec = spec.and(fromDate(entityQueryRequest.getFromDate()));
//        spec = spec.and(toDate(entityQueryRequest.getToDate()));
        if(entityQueryRequest.getFromDate() != null){
            spec= spec.and(byDateBetween(entityQueryRequest.getFromDate(), entityQueryRequest.getToDate()));
        }

        return spec;
    }
}
