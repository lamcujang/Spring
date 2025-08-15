package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.Uom;
import com.dbiz.app.productservice.domain.Warehouse;
import org.common.dbiz.request.productRequest.WarehouseQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseSpecification {
    public static Specification<Warehouse> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Warehouse> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower( root.get("name")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<Warehouse> equaIsActive(String keyword) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<Warehouse> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Warehouse> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }



    public static Specification<Warehouse> inOrgIds(Integer[] orgIds) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("orgId")).value(orgIds);
    }
    public static Specification<Warehouse> getWarehouseSpecification(WarehouseQueryRequest warehouseQueryRequest) {
        Specification<Warehouse> spec = Specification.where(hasTenantId());

        if (warehouseQueryRequest.getKeyword() != null) {
            Specification<Warehouse> keywordSpec = hasNameLike(warehouseQueryRequest.getKeyword())
                    .or(hasCodeLike(warehouseQueryRequest.getKeyword()));
            spec = spec.and(keywordSpec);
        }
        if(warehouseQueryRequest.getOrgId() != null ){
            spec= spec.and(equaOrgId(warehouseQueryRequest.getOrgId()));
        }

        if(warehouseQueryRequest.getIsActive() != null)
        {
            spec= spec.and(equaIsActive(warehouseQueryRequest.getIsActive()));
        }

        return spec;
    }
}
