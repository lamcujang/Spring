package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.IndividualIndustry;
import org.common.dbiz.request.reportRequest.IndividualIndustryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class IndividualIndustrySpecification {

    public static Specification<IndividualIndustry> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<IndividualIndustry> byTaxDeclarationIndividualId(String taxDeclarationIndividualId) {
        return (root, query, criteriaBuilder) -> taxDeclarationIndividualId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taxDeclarationIndividualId"), taxDeclarationIndividualId);
    }

    public static Specification<IndividualIndustry> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<IndividualIndustry> getSpecification(IndividualIndustryQueryRequest request) {

        Specification<IndividualIndustry> spec = Specification.where(null);
    
        if(request.getTaxDeclarationIndividualId() != null){
            spec = spec.and(byTaxDeclarationIndividualId(request.getTaxDeclarationIndividualId()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}