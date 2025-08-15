package com.dbiz.app.productservice.specification;
import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.PriceListOrg;
import org.common.dbiz.request.productRequest.PriceListOrgQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class PriceListOrgSpecification {

    public static Specification<PriceListOrg> hasIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), "%" + keyword + "%");
    }

    public static Specification<PriceListOrg> hasIsAll(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isAll"), "%" + keyword + "%");
    }

    public static Specification<PriceListOrg> equalPricelistId(Integer pricelistId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("pricelistId"), pricelistId);
    }


    public static Specification<PriceListOrg> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<PriceListOrg> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<PriceListOrg> getEntitySpecification(PriceListOrgQueryRequest queryRequest) {
        Specification<PriceListOrg> spec = Specification.where(null);
        if(queryRequest.getIsAll() != null){
            spec = spec.and(hasIsAll(queryRequest .getIsAll()));
        }
        if(queryRequest.getIsActive() != null){
            spec= spec.and(hasIsActive(queryRequest.getIsActive()));
        }
        if(queryRequest.getPricelistId() != null){
            spec= spec.and(equalPricelistId(queryRequest.getPricelistId()));
        }
        if(queryRequest.getOrgId() != null && queryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(queryRequest.getOrgId()));
        }

        spec = spec.and(hasTenantId());
        return spec;
    }
}
