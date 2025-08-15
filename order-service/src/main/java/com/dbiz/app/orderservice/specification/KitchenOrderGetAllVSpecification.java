package com.dbiz.app.orderservice.specification;

import com.dbiz.app.orderservice.domain.ReservationOrder;
import com.dbiz.app.orderservice.domain.view.KitchenOrderGetAllV;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.helper.DateHelper;
import org.common.dbiz.request.orderRequest.KitchenOrderRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.time.Instant;
import java.util.Date;

public class KitchenOrderGetAllVSpecification {
    public static Specification<KitchenOrderGetAllV> equalWarehouseId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("warehouseId"), keyword);
    }

    public static Specification<KitchenOrderGetAllV> equalFloorId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("floor").get("id"), keyword);
    }

    public static Specification<KitchenOrderGetAllV> equalTable(Integer keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("table").get("id"), keyword);
    }

    public static Specification<KitchenOrderGetAllV> hasDocumentno(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("documentno")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<KitchenOrderGetAllV> hasOrderStatus(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("orderStatus")), "%" + keyword.toLowerCase() + "%");
    }


    public static Specification<KitchenOrderGetAllV> hasIds(Integer[] ids) {
        return (root, query, criteriaBuilder) -> root.get("id").in(ids);
    }


    public static Specification<KitchenOrderGetAllV> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<KitchenOrderGetAllV> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }

    public static Specification<KitchenOrderGetAllV> hasPosOrderId(Integer keyword) {
        return (root, query, criteriaBuilder) -> (keyword == null|| keyword == 0 ) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("posOrderId"), keyword);
    }

    public static Specification<KitchenOrderGetAllV>hasIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }

    public static Specification<KitchenOrderGetAllV> byDateBetween(String fromDate, String toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null ) {
                return criteriaBuilder.conjunction();
            }
            Instant fromInstant = DateHelper.toInstant2(fromDate + " 00:00:00");
            Instant toInstant = DateHelper.toInstant2((toDate != null ? toDate : fromDate) + " 23:59:59");

            Expression<Instant> orderDateExpression = root.get("dateordered");
            return criteriaBuilder.between(orderDateExpression, fromInstant, toInstant);

        };
    }


    public static Specification<KitchenOrderGetAllV> getEntity(KitchenOrderRequest request) {
        Specification<KitchenOrderGetAllV> spec = Specification.where(null);

        if (request.getWarehouseId() != null) {
            spec = spec.and(equalWarehouseId(request.getWarehouseId()));
        }
        if (request.getFloorId() != null) {
            spec = spec.and(equalFloorId(request.getFloorId()));
        }
        if (request.getTableId() != null) {
            spec = spec.and(equalTable(request.getTableId()));
        }
        if (request.getDocumentno() != null) {
            spec = spec.and(hasDocumentno(request.getDocumentno()));
        }
        if (request.getOrgId() != null ) {
            spec = spec.and(hasOrgId(request.getOrgId()));
        }
        if (request.getIds() != null) {
            spec = spec.and(hasIds(request.getIds()));
        }
        spec = spec.and(hasPosOrderId(request.getPosOrderId()));
        spec = spec.and(hasTenantId());
        spec = spec.and(byDateBetween(request.getFromDate(), request.getToDate()));
        if(request.getOrderStatus()!=null && request.getOrderStatus().length > 0)
          spec = spec.and(hasOrderStatus(request.getOrderStatus()[0]));
        return spec;
    }
}
