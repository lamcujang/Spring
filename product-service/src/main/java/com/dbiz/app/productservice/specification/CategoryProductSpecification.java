package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.ProductCategory;
import org.common.dbiz.request.productRequest.ProductCategoryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;

public class CategoryProductSpecification {
    public static Specification<ProductCategory> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) ->criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }
    public static Specification<ProductCategory> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
    }

    public static Specification<ProductCategory> hasIsMenu(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isMenu"), keyword);
    }

    public static Specification<ProductCategory> hasIsCommodities(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isCommodities"), keyword);
    }

    public static Specification<ProductCategory> hasIsSummary(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isSummary"), keyword);
    }

    public static Specification<ProductCategory> hasProductCategoryParentId(Integer keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productCategoryParentId"), keyword);
    }

    public static Specification<ProductCategory> hasIsGroup(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isGroup"), keyword);
    }

    public static Specification<ProductCategory> hasIsActive(String keyword)
    {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), keyword);
    }
    public static Specification<ProductCategory> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<ProductCategory> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }

    public static Specification<ProductCategory> inProductCategoryId(List<Integer> productCategoryId ) {
        return (root, query, criteriaBuilder) -> root.get("id").in(productCategoryId);
    }

    public static Specification<ProductCategory> notInProductCategoryId(List<Integer> productCategoryId ) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("id").in(productCategoryId));
    }

    public static Specification<ProductCategory> getEntitySpecification(ProductCategoryQueryRequest productCategoryQueryRequest) {
        Specification<ProductCategory> spec = Specification.where(null);
        spec = spec.and(hasTenantId());
        if (productCategoryQueryRequest.getName() != null){
            spec = spec.and(hasNameLike(productCategoryQueryRequest
                    .getName()));
        }
        if(productCategoryQueryRequest.getCode() != null){
            spec= spec.and(hasCodeLike(productCategoryQueryRequest.getCode()));
        }
        if (productCategoryQueryRequest.getIsMenu() != null) {
            spec = spec.and(hasIsMenu(productCategoryQueryRequest.getIsMenu()));
        }
        if (productCategoryQueryRequest.getIsCommodities() != null) {
            spec = spec.and(hasIsCommodities(productCategoryQueryRequest.getIsCommodities()));
        }
        if (productCategoryQueryRequest.getIsGroup() != null) {
            spec = spec.and(hasIsGroup(productCategoryQueryRequest.getIsGroup()));
        }
        if(productCategoryQueryRequest.getProductCategoryParentId()!= null)
            spec = spec.and(hasProductCategoryParentId(productCategoryQueryRequest.getProductCategoryParentId()));
        if(productCategoryQueryRequest.getIsSummary() != null)
            spec = spec.and(hasIsSummary(productCategoryQueryRequest.getIsSummary()));
        if(productCategoryQueryRequest.getIsActive() != null)
            spec = spec.and(hasIsActive(productCategoryQueryRequest.getIsActive()));
        return spec;
    }
}
