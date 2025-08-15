package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.ReservationOrder;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.view.ReservationVAll;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.ReservationOrderQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReservationVAllSpecification {

    private static Specification<ReservationVAll> id(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    private static Specification<ReservationVAll> customerId(Integer customerId) {
        return (root, query, criteriaBuilder) -> customerId == null ? null : criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }

    private static Specification<ReservationVAll> ReservationVAllDate(String reservationTime) {
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




    public static Specification<ReservationVAll> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    private static Specification<ReservationVAll> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    private static Specification<ReservationVAll> floorId(Integer floorId) {
        return (root, query, criteriaBuilder) -> floorId == null ? null : criteriaBuilder.equal(root.get("floor").get("id"), floorId);
    }

    private static Specification<ReservationVAll> tableId(Integer tableId) {
        return (root, query, criteriaBuilder) -> tableId == null ? null : criteriaBuilder.equal(root.get("table").get("id"), tableId);
    }

    private static Specification<ReservationVAll> status(String[] status) {
        List<String> statusList = Arrays.asList(status);
        return (root, query, criteriaBuilder) -> root.get("status").in(statusList);
    }

    private static Specification<ReservationVAll>customerName(String customerName) {
        return (root, query, criteriaBuilder) -> customerName == null ? null : criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%");
    }

    public static Specification<ReservationVAll> byDateBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) {
                return criteriaBuilder.conjunction();
            }

//            // Parse `fromDate` and `toDate` into Instant, defaulting `toDate` to `fromDate` if null
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

    public static Specification<ReservationVAll> getEntitySpecification(ReservationOrderQueryRequest entityQueryRequest) {
        Specification<ReservationVAll> spec = Specification.where(null);
        spec = spec.and(hasTenantId());
        if(entityQueryRequest.getReservationTime() != null){
            spec= spec.and(ReservationVAllDate(entityQueryRequest.getReservationTime()));
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

        if(entityQueryRequest.getFromDate() != null){
            spec= spec.and(byDateBetween(entityQueryRequest.getFromDate(), entityQueryRequest.getToDate()));
        }

        if(entityQueryRequest.getId() != null)
        {
            spec= spec.and(id(entityQueryRequest.getId()));
        }
        return spec;
    }
}
