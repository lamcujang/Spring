package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.ProductCombo;
import org.common.dbiz.request.productRequest.ProductComboQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;

public class ProductComboSpecification {


    public static Specification<ProductCombo> hasIsItem(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isItem"),  keyword );
    }

    public static Specification<ProductCombo> hasProductId(Integer keyword) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productId"), keyword);
    }

    public static Specification<ProductCombo> hasProductComponentId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productComponentId"), keyword);
    }
    public static Specification<ProductCombo> distinctByProductId() {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            query.groupBy(root.get("productId"),
                    root.get("id"),
                    root.get("productComponentId"),
                    root.get("description"),
                    root.get("productComboUu"),
                    root.get("isItem"),
                    root.get("qty"));
            return criteriaBuilder.conjunction();
        };
    }
//    public static Specification<ProductCombo> distinctByProductId() {
//        return (root, query, criteriaBuilder) -> {
//            CriteriaQuery<ProductCombo> criteriaQuery = (CriteriaQuery<ProductCombo>) query;
//
//            criteriaQuery.distinct(true);
//            root.fetch("component", JoinType.LEFT);
//
//            criteriaQuery.select(root);
//
//            return criteriaBuilder.conjunction();
//        };
//    }

    public static Specification<ProductCombo> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<ProductCombo> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<ProductCombo> equalIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> isActive==null ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<ProductCombo> getEntitySpecification(ProductComboQueryRequest queryRequest) {
        Specification<ProductCombo> spec = Specification.where(null);

        if(queryRequest.getIsItems() != null){
            spec= spec.and(hasIsItem(queryRequest.getIsItems()));
        }
        if(queryRequest.getProductId() != null){
            spec= spec.and(hasProductId(queryRequest.getProductId()));
        }
        if(queryRequest.getProductComponentId() != null){
            spec= spec.and(hasProductComponentId(queryRequest.getProductComponentId()));
        }
//        if(queryRequest.getOrgId() != null && queryRequest.getOrgId() != 0){
//            spec= spec.and(equaOrgId(queryRequest.getOrgId()));
//        }
//        spec = spec.and(distinctByProductId());
        spec = spec.and(hasTenantId());
        spec= spec.and(equalIsActive(queryRequest.getIsActive()));
        return spec;
    }
}
