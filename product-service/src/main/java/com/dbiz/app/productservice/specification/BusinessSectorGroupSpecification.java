package com.dbiz.app.productservice.specification;

import com.dbiz.app.productservice.domain.BusinessSectorGroup;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.productRequest.BusinessSectorGroupQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class BusinessSectorGroupSpecification {
    public static Specification<BusinessSectorGroup> hasNameLike(String keyword) {

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<BusinessSectorGroup> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }
    public static Specification<BusinessSectorGroup> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification<BusinessSectorGroup> hasCode(Integer code) {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<BusinessSectorGroup> getEntitySpecification(BusinessSectorGroupQueryRequest entityQueryRequest) {
        Specification<BusinessSectorGroup> spec = Specification.where(null);
        if(entityQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(entityQueryRequest
                    .getName()));
        }

        if(entityQueryRequest.getIsActive()!=null)
        {
            spec= spec.and(hasIsActive(entityQueryRequest.getIsActive()));
        }

        if(entityQueryRequest.getCode() != null){
            spec = spec.and(hasCode(entityQueryRequest.getCode()));
        }

        spec = spec.and(hasTenantId());

        return spec;
    }
}
