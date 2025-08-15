package com.dbiz.app.orderservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.view.RequestOrderGetAllV;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.RequestOrderGetALlVQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Date;

public class RequestOrderGetAllVSpecification {

    private static Specification<RequestOrderGetAllV> likeCustomerName(String keyword) {
        return (root, query, criteriaBuilder) -> keyword == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("customer").get("name")), "%"+keyword.toLowerCase()+"%");
    }

    private static Specification<RequestOrderGetAllV> likePhone(String keyword) {
        return (root, query, criteriaBuilder) -> keyword == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("customer").get("phone1")), "%"+keyword.toLowerCase()+"%");
    }

    private static Specification<RequestOrderGetAllV> equalStatus(String status)
    {
        return (root,query,criteriaBuilder) -> status == null ? null : criteriaBuilder.equal(root.get("orderStatus"), status);
    }
    private static Specification<RequestOrderGetAllV> equalTable(Integer tableId)
    {
        return (root,query,criteriaBuilder) -> tableId == null ? null : criteriaBuilder.equal(root.get("table").get("id"), tableId);
    }
    private static Specification<RequestOrderGetAllV> equalFloor(Integer floorId)
    {
        return (root,query,criteriaBuilder) -> floorId == null ? null : criteriaBuilder.equal(root.get("floor").get("id"), floorId);
    }


    private static Specification<RequestOrderGetAllV> ReservationVAllDate(String reservationTime) {
//        return (root, query, criteriaBuilder) -> reservationTime == null ? null : criteriaBuilder.equal(root.get("reservationTime"),   DateHelper.toInstant(reservationTime));
        return (root, query, criteriaBuilder) -> {
            if (reservationTime == null) {
                return null;
            }  Instant reservationInstant = DateHelper.toInstant(reservationTime);
            Expression<Date> reservationTimeDate = criteriaBuilder.function("DATE", Date.class, root.get("orderTime"));
            Expression<Date> reservationDateOnly = criteriaBuilder.literal(Date.from(reservationInstant));
            return criteriaBuilder.equal(reservationTimeDate, reservationDateOnly);
        };
    }

    public static Specification<RequestOrderGetAllV> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    private static Specification<RequestOrderGetAllV> orgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> orgId == null ? null : criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<RequestOrderGetAllV> getEntitySpecification(RequestOrderGetALlVQueryRequest entityQueryRequest) {
        Specification<RequestOrderGetAllV> spec = Specification.where(null);

        if(entityQueryRequest.getOrderTime() != null){
            spec= spec.and(ReservationVAllDate(entityQueryRequest.getOrderTime()));
        }
        if(entityQueryRequest.getTableId() != null){
            spec= spec.and(equalTable(entityQueryRequest.getTableId()));
        }
        if(entityQueryRequest.getFloodId()!= null){
            spec= spec.and(equalFloor(entityQueryRequest.getFloodId()));
        }
        if(entityQueryRequest.getOrgId()!= null && entityQueryRequest.getOrgId() != 0)
        {
            spec= spec.and(orgId(entityQueryRequest.getOrgId()));
        }
        if(entityQueryRequest.getKeyword()!=null)
        {
            spec= spec.and(likeCustomerName(entityQueryRequest.getKeyword()).or(likePhone(entityQueryRequest.getKeyword())));
        }
        if(entityQueryRequest.getStatus()!=null)
        {
            spec= spec.and(equalStatus(entityQueryRequest.getStatus()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
