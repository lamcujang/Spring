package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.TaxDeclarationExcise;
import org.common.dbiz.request.reportRequest.TaxDeclarationExciseQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxDeclarationExciseSpecification {

    public static Specification<TaxDeclarationExcise> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<TaxDeclarationExcise> byTaxDeclarationIndividualId(String taxDeclarationIndividualId) {
        return (root, query, criteriaBuilder) -> taxDeclarationIndividualId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taxDeclarationIndividualId"), taxDeclarationIndividualId);
    }

    public static Specification<TaxDeclarationExcise> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxDeclarationExcise> getSpecification(TaxDeclarationExciseQueryRequest request) {

        Specification<TaxDeclarationExcise> spec = Specification.where(null);
    
        if(request.getTaxDeclarationIndividualId() != null){
            spec = spec.and(byTaxDeclarationIndividualId(request.getTaxDeclarationIndividualId()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}