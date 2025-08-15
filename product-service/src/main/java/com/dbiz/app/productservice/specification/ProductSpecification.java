package com.dbiz.app.productservice.specification;


import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.Product;
import org.common.dbiz.request.productRequest.ProductQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSpecification {

    public static Specification<Product> hasNameOrCodeLike(String keyword) {
        String pattern = "%" + keyword.toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), pattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern)
        );
    }

    public static Specification<Product> hasNameLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<Product> hasBrandLike(String keyword) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + keyword + "%");

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("brand")),
                "%" + keyword.toLowerCase() + "%"
        );
    }

    public static Specification<Product> hasCodeLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("code"), "%" + keyword + "%");
    }

    public static Specification<Product> hasProductType(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("productType"), keyword);
    }

    public static Specification<Product> hasProductTypeNotMaterial() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("productType"), "MTL");
    }

    public static Specification<Product> hasProductGroupType(String[] keywords) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("groupType"));
            for (String keyword : keywords) {
                inClause.value(keyword);
            }
            return inClause;
        };
    }

    public static Specification<Product> hasTaxCodeEqual(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taxCode"), "%" + keyword + "%");
    }

    public static Specification<Product> betweenSalePrice(BigDecimal salePriceFrom, BigDecimal salePriceTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("saleprice"), salePriceFrom, salePriceTo);
    }

    public static Specification<Product> betweenCostPrice(BigDecimal salePriceFrom, BigDecimal salePriceTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("costprice"), salePriceFrom, salePriceTo);
    }

    public static Specification<Product> hasCategory(Integer category) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("productCategoryId"), category);
    }

    public static Specification<Product> hasCategory(String categories) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Integer> categoryIds = Arrays.stream(categories.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            return root.get("productCategoryId").in(categoryIds);
        };
    }

    public static Specification<Product> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<Product> hasBrandId(Integer brandId) {
        return (root, query, criteriaBuilder) -> (brandId == null) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("brandId"), brandId);
    }

    public static Specification<Product> inProductIds(List<Integer> productIds) {
        return (root, query, criteriaBuilder) -> root.get("id").in(productIds);
    }

//    public static Specification<Product> hasAttr1AndNotParent() {
//        return (root, query, criteriaBuilder) -> {
//            Subquery<Integer> subquery = query.subquery(Integer.class);
//            Root<Product> subRoot = subquery.from(Product.class);
//            subquery.select(subRoot.get("productParentId")).where(criteriaBuilder.isNotNull(subRoot.get("productParentId")));
//
//            return criteriaBuilder.and(
//                    criteriaBuilder.isNotNull(root.get("attribute1")),
//                    criteriaBuilder.not(root.get("id").in(subquery))
//            );
//        };
//    }


    public static Specification<Product> isPurchased(String isPurchased) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isPurchased"), isPurchased);
    }

    public static Specification<Product> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }

    public static Specification <Product> equalIsTopping(String isTopping) {
        return (root, query, criteriaBuilder) -> isTopping == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isTopping"), isTopping);
    }
    public static Specification<Product> equaOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<Object, Object> orgJoin = root.join("assignOrgProducts", JoinType.INNER);
            return orgJoin.get("orgId").in(orgId);
        };
    }

    public static Specification<Product> notIds(List<Integer> notInIds) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("id").in(notInIds));
    }

    public static Specification<Product> hasAttr1AndNotParent() {
        return (root, query, criteriaBuilder) -> {
            // Subquery để loại bỏ các product có parent
            Subquery<Integer> subquery = query.subquery(Integer.class);

            Root<Product> subRoot = subquery.from(Product.class);
            subquery.select(subRoot.get("id"))
                    .where(criteriaBuilder.isNotNull(subRoot.get("productParentId")));

            // Điều kiện cho groupType và isTopping
//            Predicate groupTypeCondition = criteriaBuilder.or(
//                    criteriaBuilder.equal(root.get("groupType"), "PRD"),
//                    criteriaBuilder.equal(root.get("groupType"), "CBP"),
//                    criteriaBuilder.equal(root.get("groupType"), "RGD"),
//                    criteriaBuilder.equal(root.get("isTopping"), "Y")
//            );

            // Điều kiện tổng hợp
//            return criteriaBuilder.or(
//                    criteriaBuilder.and(
//                            criteriaBuilder.not(root.get("id").in(subquery)),  // Không có parent
//                            criteriaBuilder.isNotNull(root.get("attribute1"))  // attribute1 không null
//                    )
////                   , groupTypeCondition  // groupType là PRD, CBP, RGD hoặc isTopping = Y
//            );
            return criteriaBuilder.or(
                    criteriaBuilder.and(
                            criteriaBuilder.not(root.get("id").in(subquery)),
                            criteriaBuilder.isNotNull(root.get("attribute1"))
                    ),
                    criteriaBuilder.and(
                            criteriaBuilder.isNull(root.get("attribute1")),
                            criteriaBuilder.isNull(root.get("productParentId"))
                    )
            );

        };
    }
    public static Specification<Product> getProductSpecification(ProductQueryRequest productQueryRequest) {
        Specification<Product> spec = Specification.where(null);
        if (productQueryRequest.getKeyword() != null && !productQueryRequest.getKeyword().isEmpty()) {
            spec = spec.and(hasNameOrCodeLike(productQueryRequest.getKeyword()));
        }
        if (productQueryRequest.getName() != null && !productQueryRequest.getName().isEmpty()) {
            spec = spec.and(hasNameOrCodeLike(productQueryRequest.getName()));
        }
//        if (productQueryRequest.getName() != null && !productQueryRequest.getName().isEmpty()) {
//            spec = spec.and(hasNameLike(productQueryRequest
//                    .getName()));
//        }
        if (productQueryRequest.getCode() != null ) {
            spec = spec.and(hasCodeLike(productQueryRequest.getCode()));
        }
        if (productQueryRequest.getProductType() != null) {
            spec = spec.and(hasProductType(productQueryRequest.getProductType()));
        }
        if (productQueryRequest.getIsInCombo() != null && "Y".equals(productQueryRequest.getIsInCombo())) {
            spec = spec.and(hasProductTypeNotMaterial());
        }
        if (productQueryRequest.getTaxCode() != null) {
            spec = spec.and(hasTaxCodeEqual(productQueryRequest.getTaxCode()));
        }
        if (productQueryRequest.getSalePriceFrom() != null && productQueryRequest.getSalePriceTo() != null) {
            spec = spec.and(betweenSalePrice(productQueryRequest.getSalePriceFrom(), productQueryRequest.getSalePriceTo()));
        }
        if (productQueryRequest.getCostPriceFrom() != null && productQueryRequest.getCostPriceTo() != null) {
            spec = spec.and(betweenCostPrice(productQueryRequest.getCostPriceFrom(), productQueryRequest.getCostPriceTo()));
        }
        if (productQueryRequest.getOrgId() != null && productQueryRequest.getOrgId() != 0) {
            spec = spec.and(equaOrgId(productQueryRequest.getOrgId()));
        }
        if (productQueryRequest.getIsPurchased() != null) {
            spec = spec.and(isPurchased(productQueryRequest.getIsPurchased()));
        }
        if (productQueryRequest.getCategoryId() != null) {
            spec = spec.and(hasCategory(productQueryRequest.getCategoryId()));
        }
        if (productQueryRequest.getGroupType() != null) {
            spec = spec.and(hasProductGroupType(productQueryRequest.getGroupType()));
        }
        if (productQueryRequest.getBrand() != null) {
            spec = spec.and(hasBrandLike(productQueryRequest.getBrand()));
        }
        if(productQueryRequest.getBrandId() != null){
            spec = spec.and(hasBrandId(productQueryRequest.getBrandId()));
        }
        spec = spec.and(equalIsTopping(productQueryRequest.getIsTopping()));
        if(productQueryRequest.getWarehouseId() == null )
            spec = spec.and(hasAttr1AndNotParent());
        spec = spec.and(hasTenantId());
        spec = spec.and(hasIsActive(productQueryRequest.getIsActive()));
        return spec;
    }


}
