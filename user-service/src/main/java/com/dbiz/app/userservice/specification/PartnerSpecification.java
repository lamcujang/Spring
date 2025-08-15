package com.dbiz.app.userservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.userservice.domain.PartnerGroup;
import org.common.dbiz.request.userRequest.PartnerGroupQuery;
import org.springframework.data.jpa.domain.Specification;

public class PartnerSpecification {
    public static Specification<PartnerGroup> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("groupName")), "%" + keyword.toLowerCase() + "%");
    }
    public static Specification<PartnerGroup> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("groupCode"),  "%" + keyword + "%");
    }

    public static Specification<PartnerGroup> hasTenantId() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<PartnerGroup> hasIsCustomer(String isCustomer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isCustomer"), isCustomer);
    }
    public static Specification<PartnerGroup> getEntitySpecification(PartnerGroupQuery QueryRequest) {
        Specification<PartnerGroup> spec = Specification.where(null);
        if(QueryRequest.getKeyword() != null){
            spec = spec.or(hasNameLike(QueryRequest.getKeyword()));
            spec = spec.or(hasCodeLike(QueryRequest.getKeyword()));
        }
        if(QueryRequest.getIsCustomer()!=null){
            spec = spec.and(hasIsCustomer(QueryRequest.getIsCustomer()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}