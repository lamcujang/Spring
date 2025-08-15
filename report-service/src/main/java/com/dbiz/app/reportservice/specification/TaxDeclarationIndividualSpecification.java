package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.TaxDeclarationIndividual;
import org.common.dbiz.request.reportRequest.TaxDeclarationIndividualQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxDeclarationIndividualSpecification {

    public static Specification<TaxDeclarationIndividual> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<TaxDeclarationIndividual> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxDeclarationIndividual> getSpecification(TaxDeclarationIndividualQueryRequest request) {

        Specification<TaxDeclarationIndividual> spec = Specification.where(null);
    
        if(request.getId() != null){
            spec = spec.and(byId(request.getId()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}