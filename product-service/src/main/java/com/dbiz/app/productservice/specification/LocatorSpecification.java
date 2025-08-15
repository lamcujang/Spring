package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.Locator;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.LocatorQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class LocatorSpecification {
    public static Specification<Locator> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
    }

    public static Specification<Locator> equaX(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("x"),  keyword );
    }
    public static Specification<Locator> equaY(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("y"),  keyword );
    }
    public static Specification<Locator> equaZ(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("z"),  keyword );
    }

    public static Specification<Locator> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Locator> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }

    public static Specification<Locator> equalWarehouse(Integer warehouseId)
    {
        return (root, query,criteriaBuilder)-> criteriaBuilder.equal(root.get("warehouseId"), warehouseId);
    }
    public static Specification<Locator> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }
    public static Specification<Locator> getLocatorSpecification(LocatorQueryRequest locatorQueryRequest) {
        Specification<Locator> spec = Specification.where(null);

        if(locatorQueryRequest.getCode() != null){
            spec= spec.and(hasCodeLike(locatorQueryRequest.getCode()));
        }


        if(locatorQueryRequest.getX() != null){
            spec= spec.and(equaX(locatorQueryRequest.getCode()));
        }

        if(locatorQueryRequest.getY() != null){
            spec= spec.and(equaY(locatorQueryRequest.getCode()));
        }

        if(locatorQueryRequest.getZ() != null){
            spec= spec.and(equaZ(locatorQueryRequest.getCode()));
        }
        if(locatorQueryRequest.getOrgId() != null && locatorQueryRequest.getOrgId() != 0)
        {
            spec = spec.and(equaOrgId(locatorQueryRequest.getOrgId()));
        }
        if(locatorQueryRequest.getWarehouseId()!=null )
        spec = spec.and(equaIsActive(locatorQueryRequest.getIsActive() != null ? locatorQueryRequest.getIsActive() : "Y"));

        spec = spec.and(hasTenantId());
        return spec;
    }
}
