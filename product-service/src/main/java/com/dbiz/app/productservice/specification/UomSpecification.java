package com.dbiz.app.productservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.Uom;
import org.common.dbiz.request.productRequest.UomQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class UomSpecification {
    public static Specification<Uom> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<Uom> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
    }
    public static Specification<Uom> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<Uom> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }
    public static Specification<Uom> getUomSpecification(UomQueryRequest uomQueryRequest) {
        Specification<Uom> spec = Specification.where(null);
        if(uomQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(uomQueryRequest
                    .getName()));
        }
        if(uomQueryRequest.getCode() != null){
            spec= spec.and(hasCodeLike(uomQueryRequest.getCode()));
        }
        if(uomQueryRequest.getOrgId() != null && uomQueryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(uomQueryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
