package com.dbiz.app.reportservice.specification;

import com.dbiz.app.reportservice.domain.TaxBusinessIndustry;
import org.common.dbiz.request.reportRequest.TaxBusinessIndustryQueryRequest;
import org.springframework.data.jpa.domain.Specification;

public class TaxBusinessIndustrySpecification {

    public static Specification<TaxBusinessIndustry> byId(Integer id) {
        return (root, query, criteriaBuilder) -> id == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<TaxBusinessIndustry> byIndustryCode(String industryCode) {
        return (root, query, criteriaBuilder) -> industryCode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("industryCode"), industryCode);
    }

    public static Specification<TaxBusinessIndustry> hasNameLike(String industryName) {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("industryName")),
                "%" + industryName.toLowerCase() + "%"
        );
    }

    public static Specification<TaxBusinessIndustry> hasIsActive(String isActive) {
        return (root, query, criteriaBuilder) -> (isActive== null || isActive.isEmpty()) ? criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<TaxBusinessIndustry> getSpecification(TaxBusinessIndustryQueryRequest request) {

        Specification<TaxBusinessIndustry> spec = Specification.where(null);
    
        if(request.getIndustryCode() != null){
            spec = spec.and(byIndustryCode(request.getIndustryCode()));
        }

        if(request.getIndustryName() != null){
            spec = spec.and(hasNameLike(request.getIndustryName()));
        }

        if(request.getIsActive() != null){
            spec = spec.and(hasIsActive(request.getIsActive()));
        }

        return spec;
    }
}