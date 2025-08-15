package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.Floor;
import com.dbiz.app.orderservice.domain.view.ReservationTablePosV;
import com.dbiz.app.orderservice.domain.view.TablePosV;
import com.dbiz.app.orderservice.domain.view.TablePosVV2;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.TableQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class TablePosVV2Specification {
    public static Specification<TablePosVV2> hasTableLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("tableNo"), "%" + keyword + "%");
    }
    public static Specification<TablePosVV2> equalTable(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), keyword);
    }
    public static Specification<TablePosVV2> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<TablePosVV2> hasTableStatusEqua(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tableStatus"),   keyword );
    }

    public static Specification<TablePosVV2> equaFloorId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floorId"), keyword );
    }
    public static Specification<TablePosVV2> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<TablePosVV2> equaIsDefault(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDefault"), keyword);
    }
    public static Specification<TablePosVV2> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }

    public static Specification<TablePosVV2> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

//    private static Specification<TablePosVV2> ReservationVAllDate(String reservationTime) {
////        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
//        return (root, query, criteriaBuilder) -> {
//            if (reservationTime == null) {
//                return null;
//            }  Instant reservationInstant = DateHelper.toInstant(reservationTime);
//            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("reservationTime"));
//            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
//
//            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
//        };
//    }

    public static Specification<TablePosVV2> findByReservationDate(String reservationDate) {
        return (root, query, criteriaBuilder) -> {
            Join<TablePosV, ReservationTablePosV> reservationJoin = root.join("reservation", JoinType.LEFT);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, reservationJoin.get("reservationTime"));
            Instant reservationInstant = DateHelper.toInstant(reservationDate);
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            Predicate reservationDatePredicate = criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
            return criteriaBuilder.or(reservationDatePredicate, reservationJoin.isNull());
        };
    }
    public static Specification<TablePosVV2>hasFloorByPosterminalId(Integer posterminalId)
    {

        return (root, query, criteriaBuilder) -> {
            if (posterminalId == null) {
                return criteriaBuilder.conjunction();  // Return all if posterminalId is null
            }

            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Floor> floorRoot = subquery.from(Floor.class);
            subquery.select(floorRoot.get("id"))
                    .where(criteriaBuilder.equal(
                            criteriaBuilder.coalesce(floorRoot.get("posTerminalId"), posterminalId),
                            posterminalId
                    ));

            return criteriaBuilder.in(root.get("floorId")).value(subquery);
        };
    }

    public static Specification<TablePosVV2> getTableSpecification(TableQueryRequest  tableQueryRequest) {
        Specification<TablePosVV2> spec = Specification.where(null);

        if(tableQueryRequest.getTableNo() != null){
            spec= spec.and(hasTableLike(tableQueryRequest.getTableNo()));
        }
        if(tableQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(tableQueryRequest.getName()));
        }
        if(tableQueryRequest.getTableStatus()!= null && !tableQueryRequest.getTableStatus().isEmpty()){
            spec= spec.and(hasTableStatusEqua(tableQueryRequest.getTableStatus()));
        }
        if(tableQueryRequest.getOrgId()!= null )
        {
            spec= spec.and(hasOrgId(tableQueryRequest.getOrgId()));
        }
        if(tableQueryRequest.getFloorId()!=null)
        {
            spec= spec.and(equaFloorId(tableQueryRequest.getFloorId()));
        }
        if(tableQueryRequest.getIsActive() != null)
        {
            spec= spec.and(equaIsActive(tableQueryRequest.getIsActive()));
        }
        if(tableQueryRequest.getIsDefault() != null)
        {
            spec= spec.and(equaIsDefault(tableQueryRequest.getIsDefault()));
        }
//        if(tableQueryRequest.getReservationDate() !=null)
//        {
//            spec= spec.or(findByReservationDate(tableQueryRequest.getReservationDate()));
//        }
        if(tableQueryRequest.getTableId()!=null)
        {
            spec=spec.and(equalTable(tableQueryRequest.getTableId()));
        }
//        spec = spec.and(hasFloorByPosterminalId(tableQueryRequest.getPosTerminalId()));
        spec = spec.and(hasTenantId());
        return spec;
    }
}
