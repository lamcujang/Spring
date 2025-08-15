package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.BusinessSector;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.BusinessSectorQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class BusinessSectorSpecification {
    public static Specification<BusinessSector> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<BusinessSector> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }
    public static Specification<BusinessSector> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<BusinessSector> equaOrgId(Integer orgId ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<BusinessSector> hasCode(String code) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<BusinessSector> hasBusinessSectorGroupId(Integer businessSectorGroupId) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("businessSectorGroupId"), businessSectorGroupId);
    }


    public static Specification<BusinessSector> getEntitySpecification(BusinessSectorQueryRequest entityQueryRequest) {
        Specification<BusinessSector> spec = Specification.where(null);
        if(entityQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(entityQueryRequest
                    .getName()));
        }

        if(entityQueryRequest.getIsActive()!=null)
        {
            spec= spec.and(hasIsActive(entityQueryRequest.getIsActive()));
        }

        if(entityQueryRequest.getOrgId() != null && entityQueryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(entityQueryRequest.getOrgId()));
        }

        if(entityQueryRequest.getCode() != null){
            spec = spec.and(hasCode(entityQueryRequest.getCode()));
        }

        if(entityQueryRequest.getBusinessSectorGroupId() != null){
            spec = spec.and(hasBusinessSectorGroupId(entityQueryRequest.getBusinessSectorGroupId()));
        }
        spec = spec.and(hasTenantId());



        return spec;
    }
}
