package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.EnvironmentFee;
import com.dbiz.app.tenantservice.domain.AuditContext;
import org.common.dbiz.request.reportRequest.EnvironmentFeeQueryRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class EnvironmentFeeSpecification {

    public static Specification<EnvironmentFee> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<EnvironmentFee> byItemCode(String itemCode) {
        return (root, query, criteriaBuilder) -> itemCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("itemCode"), itemCode);
    }

    public static Specification<EnvironmentFee> hasNameLike(String itemName) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("itemName")),
                "%" + itemName.toLowerCase() + "%"
        );
    }

    private static Specification<EnvironmentFee> byTaxType(Integer taxType) {
        return (root, query, criteriaBuilder) -> taxType == null ? null : criteriaBuilder.equal(root.get("taxType"), taxType);
    }

    public static Specification<EnvironmentFee> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<EnvironmentFee> getSpecification(EnvironmentFeeQueryRequest request) {

        Specification<EnvironmentFee> spec = Specification.where(null);

        if(request.getItemCode() != null){
            spec = spec.and(byItemCode(request.getItemCode()));
        }

        if(request.getItemName() != null){
            spec = spec.and(hasNameLike(request.getItemName()));
        }

        if(request.getTaxType() != null){
            spec = spec.and(byTaxType(request.getTaxType()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}