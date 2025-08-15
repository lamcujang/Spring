package com.dbiz.app.productservice.specification;

import com.dbiz.app.tenantservice.domain.AuditContext;
import com.dbiz.app.productservice.domain.StorageOnhand;
import org.common.dbiz.request.productRequest.StorageQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class StorageOnhandSpecification {

    public static Specification<StorageOnhand> hasTenantId() {
//     tenantId   return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), AuditContext.getAuditInfo().getTenantId());
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenantId"), AuditContext.getAuditInfo().getTenantId());
    }
    public static Specification<StorageOnhand> equalOrgId(Integer orgId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orgId"), orgId);
    }


    public static Specification<StorageOnhand> getProductSpecification(StorageQueryRequest queryRequest) {
        Specification<StorageOnhand> spec = Specification.where(null);
//        if(productQueryRequest.getName() != null){
//            spec = spec.and(hasNameLike(productQueryRequest
//                    .getName()));
//        }
//        if(productQueryRequest.getCode() != null){
//            spec= spec.and(hasCodeLike(productQueryRequest.getCode()));
//        }
//        if(productQueryRequest.getProductType() != null){
//            spec= spec.and(hasProductType(productQueryRequest.getProductType()));
//        }
//        if(productQueryRequest.getTaxCode() != null){
//            spec= spec.and(hasTaxCodeEqual(productQueryRequest.getTaxCode()));
//        }
//        if(productQueryRequest.getSalePriceFrom() != null && productQueryRequest.getSalePriceTo() != null){
//            spec= spec.and(betweenSalePrice(productQueryRequest.getSalePriceFrom(), productQueryRequest.getSalePriceTo()));
//        }
//        if(productQueryRequest.getCostPriceFrom() != null && productQueryRequest.getCostPriceTo() != null){
//            spec= spec.and(betweenCostPrice(productQueryRequest.getCostPriceFrom(), productQueryRequest.getCostPriceTo()));
//        }
//        if(productQueryRequest.getCategory() != null){
//            spec= spec.and(hasCategory(productQueryRequest.getCategory()));
//        }
//        if(productQueryRequest.getIsPurchased() != null){
//            spec= spec.and(isPurchased(productQueryRequest.getIsPurchased()));
//        }
        if(queryRequest.getOrgId() != null){
            spec= spec.and(equalOrgId(queryRequest.getOrgId()));
        }
        spec = spec.and(hasTenantId());
        return spec;
    }
}
