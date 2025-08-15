package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.TaxDeclarationResourceEnvironment;
import org.common.dbiz.request.reportRequest.TaxDeclarationResourceEnvironmentQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxDeclarationResourceEnvironmentSpecification {

    public static Specification<TaxDeclarationResourceEnvironment> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<TaxDeclarationResourceEnvironment> byTaxDeclarationIndividualId(String taxDeclarationIndividualId) {
        return (root, query, criteriaBuilder) -> taxDeclarationIndividualId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taxDeclarationIndividualId"), taxDeclarationIndividualId);
    }

    public static Specification<TaxDeclarationResourceEnvironment> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxDeclarationResourceEnvironment> getSpecification(TaxDeclarationResourceEnvironmentQueryRequest request) {

        Specification<TaxDeclarationResourceEnvironment> spec = Specification.where(null);
    
        if(request.getTaxDeclarationIndividualId() != null){
            spec = spec.and(byTaxDeclarationIndividualId(request.getTaxDeclarationIndividualId()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}