package com.dbiz.app.productservice.specification;

 import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.PriceListProduct;
import org.common.dbiz.request.productRequest.PriceListProductQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.math.BigDecimal;

public class PriceListProductSpecification {
//     private String productId;
//        private String isActive;
//        private BigDecimal costprice;
//        private BigDecimal standardprice;
//        private BigDecimal salesprice;
//        private BigDecimal lastorderprice;
//        private Integer pricelistId;
    public static Specification<PriceListProduct> equalProductId(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        return (root, query, criteriaBuilder) ->criteriaBuilder.equal(root.get("productId"), keyword);
    }
    public static Specification<PriceListProduct> hasIsActive(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), "%" + keyword + "%");
    }

    public static Specification<PriceListProduct> equalCostPrice(BigDecimal keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("costPrice"),  keyword );
    }

    public static Specification<PriceListProduct> equalStandardPrice(BigDecimal keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("standardPrice"), keyword);
    }

    public static Specification<PriceListProduct> equaSalesPrice(BigDecimal keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("salesPrice"), keyword);
    }

    public static Specification<PriceListProduct> equapricelistId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priceListId"), keyword);
    }

    public static Specification<PriceListProduct> equaOrgId(Integer orgId) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("assignOrgProducts", JoinType.INNER);
            return orgJoin.get("orgId").in(orgId);
        };
    }
    public static Specification<PriceListProduct> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<PriceListProduct> getEntitySpecification(PriceListProductQueryRequest queryRequest) {
        Specification<PriceListProduct> spec = Specification.where(null);
        if(queryRequest.getProductId() != null){
            spec = spec.and(equalProductId(queryRequest.getProductId()));
        }
        if(queryRequest.getIsActive() != null){
            spec= spec.and(hasIsActive(queryRequest.getIsActive()));
        }
        if(queryRequest.getCostprice() != null){
            spec= spec.and(equalCostPrice(queryRequest.getCostprice()));
        }
        if(queryRequest.getStandardprice() != null){
            spec= spec.and(equalStandardPrice(queryRequest.getStandardprice()));
        }
        if(queryRequest.getSalesprice() != null){
            spec= spec.and(equaSalesPrice(queryRequest.getSalesprice()));
        }
        if(queryRequest.getPriceListId() != null){
            spec= spec.and(equapricelistId(queryRequest.getPriceListId()));
        }
        if(queryRequest.getOrgId() != null && queryRequest.getOrgId() != 0){
            spec= spec.and(equaOrgId(queryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }



}
