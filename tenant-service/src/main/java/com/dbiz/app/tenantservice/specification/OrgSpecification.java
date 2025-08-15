package com.dbiz.app.tenantservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.tenantservice.domain.Org;
import org.common.dbiz.request.tenantRequest.OrgQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class OrgSpecification {

    public static  Specification<Org> hasAddress(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + keyword.toLowerCase() + "%");
    }
    public static  Specification<Org> hasPhone(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + keyword.toLowerCase() + "%");
    }
    public static  Specification<Org> hasEmail(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%");
    }
    public static  Specification<Org> hasCode(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + keyword.toLowerCase() + "%");
    }
    public static  Specification<Org> hasName(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
    }

    public static  Specification<Org> hasOrgId(Integer id){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public static  Specification<Org> hasOrgIdCurrent(Integer id){
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.notEqual(root.get("id"), (id));
    }

    public static Specification<Org> hasIsActive(String isActive){
        return (root, query, criteriaBuilder) -> isActive == null ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }
    public static Specification<Org> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<Org> getEntitySpecification(OrgQueryRequest queryRequest) {
        Specification<Org> spec = Specification.where(null);

        if (queryRequest.getAddress() != null) {
            spec = spec.and(hasAddress(queryRequest.getAddress()));
        }
        if(queryRequest.getKeyword()!=null)
        {
            spec = spec.or(hasPhone(queryRequest.getKeyword()));
            spec = spec.or(hasEmail(queryRequest.getKeyword()));
            spec = spec.or(hasCode(queryRequest.getKeyword()));
            spec = spec.or(hasName(queryRequest.getKeyword()));
        }
        if(queryRequest.getId()!=null)
        {
            spec = spec.and(hasOrgId(queryRequest.getId()));
        }
        spec=spec.and(hasOrgIdCurrent(queryRequest.getOrgIdCurrent()));
        spec = spec.and(hasTenantId());
        spec = spec.and(hasIsActive(queryRequest.getIsActive()));
        return spec;
    }
}
