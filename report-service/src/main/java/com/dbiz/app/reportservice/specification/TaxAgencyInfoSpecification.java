package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.TaxAgencyInfo;
import org.common.dbiz.request.reportRequest.TaxAgencyInfoQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxAgencyInfoSpecification {

    public static Specification<TaxAgencyInfo> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<TaxAgencyInfo> byTaxAgentCode(String byTaxAgentCode) {
        return (root, query, criteriaBuilder) -> byTaxAgentCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("byTaxAgentCode"), byTaxAgentCode);
    }

    public static Specification<TaxAgencyInfo> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxAgencyInfo> getSpecification(TaxAgencyInfoQueryRequest request) {

        Specification<TaxAgencyInfo> spec = Specification.where(null);
    
        if(request.getTaxAgentCode() != null){
            spec = spec.and(byTaxAgentCode(request.getTaxAgentCode()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}