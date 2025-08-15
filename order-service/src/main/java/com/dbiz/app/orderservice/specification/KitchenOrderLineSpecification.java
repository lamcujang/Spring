package com.dbiz.app.orderservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.orderservice.domain.KitchenOrderLine;
import org.common.dbiz.request.orderRequest.KitchenOrderLineRequest;
import org.springframework.data.jpa.domain.Specification;

public class KitchenOrderLineSpecification {
    public static Specification<KitchenOrderLine> equalKitchenOrderId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("kitchenOrderId"),  keyword );
    }
    public static Specification<KitchenOrderLine> equalOrderLineStatus(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderlineStatus"),  keyword);
    }

    public static Specification<KitchenOrderLine> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<KitchenOrderLine> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<KitchenOrderLine> getEntity(KitchenOrderLineRequest request) {
        Specification<KitchenOrderLine> spec = Specification.where(null);

        if(request.getKitchenOrderId() != null){
            spec= spec.and(equalKitchenOrderId(request.getKitchenOrderId()));
        }
        if(request.getOrderlineStatus() != null){
            spec= spec.and(equalOrderLineStatus(request.getOrderlineStatus()));
        }

        if(request.getOrgId()!= null)
        {
            spec= spec.and(hasOrgId(request.getOrgId() ));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
