package com.dbiz.app.systemservice.specification;

import com.dbiz.app.systemservice.domain.TaxOffice;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.systemRequest.TaxOfficeQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxOfficeSpecification {

    public static Specification<TaxOffice> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }
    public static Specification<TaxOffice> isParent(String isParent) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("isParent"), isParent);
    }
    public static Specification<TaxOffice> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<TaxOffice> hasOrgId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), keyword);
    }
    public static Specification<TaxOffice> isActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction() :  criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxOffice> getTaxOfficeSpecification(TaxOfficeQueryRequest taxOfficeQueryRequest) {
        Specification<TaxOffice> spec = Specification.where(isActive("Y"));

        if(taxOfficeQueryRequest.getName() != null){
            spec= spec.and(hasNameLike(taxOfficeQueryRequest.getName()));
        }
        if(taxOfficeQueryRequest.getIsParent() != null){
            spec= spec.and(isParent(taxOfficeQueryRequest.getIsParent()));
        }
        if(taxOfficeQueryRequest.getIsActive() != null){
            spec= spec.and(isActive(taxOfficeQueryRequest.getIsActive()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
